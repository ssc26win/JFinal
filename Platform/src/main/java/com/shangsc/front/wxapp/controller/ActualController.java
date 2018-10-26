package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.shangsc.front.code.DateType;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/9/2 10:29
 * @Version 1.0.0
 * @Desc
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class ActualController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findLatestData() {
        SysUser byWxAccount = findByWxAccount();
        String companyName = getPara("companyName");
        Company first = Company.me.findFirst("select * from t_company where name=?", companyName);
        if (StringUtils.isNotEmpty(companyName) && first == null) {
            CommonDes commonDes = new CommonDes();
            commonDes.setCode(-1);
            commonDes.setMessage("未找到单位名称！");
            renderJson(commonDes);
        }
        String innerCode = "";
        if (first != null) {
            innerCode = first.getInnerCode();
        }
        if (StringUtils.isEmpty(innerCode)) {
            innerCode = byWxAccount.getInnerCode();
        }
        String meterAddress = getPara("meterAddress");

        StringBuffer sqlExceptSelect = new StringBuffer("select tad.*, tc.name as companyName,tc.real_code from " +
                " (select * from t_actual_data order by id desc )" +
                " tad left join t_company tc on tc.inner_code=tad.inner_code where 1=1 ");

        if (StringUtils.isNotEmpty(innerCode)) {
            sqlExceptSelect.append(" and tad.inner_code='" + innerCode + "'");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address='" + meterAddress + "'");
        }

        sqlExceptSelect.append(" group by tad.inner_code,tad.meter_address");

        List<ActualData> actualDatas = ActualData.me.find(sqlExceptSelect.toString());

        //String s = JsonUtil.obj2Json(actualDatas);

        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();

        for (ActualData data : actualDatas) {
            if (StringUtils.isEmpty(companyName)) {
                companyName = data.get("companyName");
            }
            JSONObject object = new JSONObject();
            object.put("meter_address", data.get("meter_address"));
            object.put("net_water", data.get("net_water"));
            object.put("sum_water", data.get("sum_water"));
            object.put("write_time", data.get("write_time"));
            array.add(object);
        }
        result.put("companyName", companyName);
        result.put("waterUseInfo", array);
        renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void actualChart() {
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String meterAddress = this.getPara("meterAddress");
        List<Record> records = ActualDataWx.me.findWxActualChart(wxInnerCodeSQLStr, meterAddress);
        Company byInnerCode = Company.me.findByInnerCode(wxInnerCodeSQLStr);
        String subtitle = "日用水量";
        String seriesName = "日用水量";
        Integer type = 1;
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            if (type == 2) {
                subtitle = "日供水量";
                seriesName = "日供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add(record.get("DAY").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("day", day);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void findActualList() {
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        String status = this.getPara("status", "-1");
        Page<ActualData> pageInfo = new Page<>();
        int exceptionTime = 24;
        Map<String, Object> dictMap = DictData.dao.getDictMap(null, DictCode.ACTUAL_EXCEPTION_TIME_OUT);
        if (dictMap.size() == 1) {
            Object[] objects = dictMap.keySet().toArray();
            String num = objects[0].toString();
            if (NumberUtils.isDigits(num)) {
                exceptionTime = Integer.parseInt(num);
            }
        }
        pageInfo = ActualDataWx.me.finWxActualData(getPage(), getRows(), keyword, wxInnerCodeSQLStr);
        List<ActualData> list = pageInfo.getList();
        setVoProp(list, exceptionTime);
        this.renderJson(pageInfo);
    }

    private void setVoProp(List<ActualData> list, int exceptionTime) {
        Map<String, String> stateMap = ActualState.getMap();
        long dayTime = exceptionTime * 60 * 60 * 1000;
        Date now = new Date();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("waters_type") != null) {
                    co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.get("waters_type")))));
                }
                //co.put("alarm", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getAlarm())));
                // 正常 异常（24小时内没有数据传回来时是异常） 停用（一天传回来数没有增量是停用）
                if (co.getNetWater() != null && co.getNetWater().compareTo(new BigDecimal(0.00)) == 0) {
                    co.setState(Integer.parseInt(ActualState.STOP));
                }
                if (co.getWriteTime() != null) {
                    long target = now.getTime() - co.getWriteTime().getTime();
                    if (target > dayTime) {
                        co.setState(Integer.parseInt(ActualState.EXCEPTION));
                    }
                } else {
                    co.setState(Integer.parseInt(ActualState.EXCEPTION));
                }
                if (co.getNetWater() != null && co.getNetWater().compareTo(new BigDecimal(0.00)) < 0) {
                    co.setState(Integer.parseInt(ActualState.EXCEPTION));
                }
                if (co.getId() == null) {
                    co.setState(Integer.parseInt(ActualState.DISABLE));
                }
                co.put("stateName", stateMap.get(String.valueOf(co.getState())));
                if (co.getWriteTime() == null) {
                    co.setWriteTime(DateUtils.parseDate("0001-01-01 00:00:00"));
                }
                list.set(i, co);
            }
        }
    }


    @Clear(AuthorityInterceptor.class)
    public void readSearchList() {
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxReadSearchList(getPage(), getRows(), null, null, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void readSearchChart() {
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String date = this.getPara("date");
    }

    @Clear(AuthorityInterceptor.class)
    public void daily() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxDailyActualData(wxInnerCodeSQLStr, startTime, endTime);
        Company byInnerCode = Company.me.findByInnerCode(wxInnerCodeSQLStr);
        String subtitle = "日用水量";
        String seriesName = "日用水量";
        Integer type = 1;
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            if (type == 2) {
                subtitle = "日供水量";
                seriesName = "日供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add(record.get("DAY").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("day", day);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void month() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxMonthActualData(wxInnerCodeSQLStr, startTime, endTime);
        Company byInnerCode = Company.me.findByInnerCode(wxInnerCodeSQLStr);
        Integer type = 1;
        String subtitle = "月用水量";
        String seriesName = "月用水量";
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            subtitle = "月用水量";
            seriesName = "月用水量";
            if (type == 2) {
                subtitle = "月供水量";
                seriesName = "月供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> month = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            month.add(record.get("month").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("month", month);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void year() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxYearActualData(wxInnerCodeSQLStr, startTime, endTime);
        Company byInnerCode = Company.me.findByInnerCode(wxInnerCodeSQLStr);
        Integer type = 1;
        String subtitle = "月用水量";
        String seriesName = "月用水量";
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            subtitle = "月用水量";
            seriesName = "月用水量";
            if (type == 2) {
                subtitle = "月供水量";
                seriesName = "月供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> year = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            year.add(record.get("year").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("year", year);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void dailyList() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxDailyList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void monthList() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxMonthList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void yearList() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxYearList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }


    @Clear(AuthorityInterceptor.class)
    public void searchMine() {
        int dateType = this.getParaToInt("dateType", 1).intValue();
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String keyword = this.getPara("keyword");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String subtitle = "用水量";
        String seriesName = "用水量";
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        if (DateType.YEAR == dateType) {
            List<Record> records = ActualDataWx.me.getWxYearActualData(wxInnerCodeSQLStr, startTime, endTime);
            List<String> year = new ArrayList<String>();
            for (Record record : records) {
                sumWater.add(record.get("sumWater"));
                year.add(record.get("year").toString());
            }
            obj.put("sumWater", sumWater);
            obj.put("year", year);
            obj.put("subtitle", subtitle);
            obj.put("seriesName", seriesName);

            Page<ActualData> pageInfo = ActualDataWx.me.findWxYearList(getPage(), this.getWxRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
            obj.put("jsonList", pageInfo);
            this.renderJson(obj);
        } else if (DateType.MONTH == dateType) {
            List<Record> records = ActualDataWx.me.getWxMonthActualData(wxInnerCodeSQLStr, startTime, endTime);
            List<String> month = new ArrayList<String>();
            for (Record record : records) {
                sumWater.add(record.get("sumWater"));
                month.add(record.get("month").toString());
            }
            obj.put("sumWater", sumWater);
            obj.put("month", month);
            obj.put("subtitle", subtitle);
            obj.put("seriesName", seriesName);

            Page<ActualData> pageInfo = ActualDataWx.me.findWxMonthList(getPage(), this.getWxRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
            obj.put("jsonList", pageInfo);
            this.renderJson(obj);
        } else if (DateType.YEAR == dateType) {
            List<Record> records = ActualDataWx.me.getWxDailyActualData(wxInnerCodeSQLStr, startTime, endTime);
            List<String> day = new ArrayList<String>();
            for (Record record : records) {
                sumWater.add(record.get("sumWater"));
                day.add(record.get("DAY").toString());
            }
            obj.put("sumWater", sumWater);
            obj.put("day", day);
            obj.put("subtitle", subtitle);
            obj.put("seriesName", seriesName);

            Page<ActualData> pageInfo = ActualDataWx.me.findWxDailyList(getPage(), this.getWxRows(), startTime, endTime, keyword, wxInnerCodeSQLStr);
            obj.put("jsonList", pageInfo);
            this.renderJson(obj);
        } else {
            this.renderJson(InvokeResult.failure("错误日期类型"));
        }
    }
}
