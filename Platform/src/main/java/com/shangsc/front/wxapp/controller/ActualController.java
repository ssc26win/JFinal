package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.front.code.DateType;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
    public void findById() {
        Long id = this.getParaToLong("id");
        ActualData byId = ActualData.me.findById(id);
        if (byId != null) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictCode.ChargeType);
            Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
            Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
            String meterAddress = byId.getMeterAddress();
            if (StringUtils.isNotEmpty(meterAddress)) {
                WaterMeter co = WaterMeter.me.findByMeterAddress(meterAddress);
                if (co.getWatersType() != null && mapWatersType.size() > 0) {
                    co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                }
                if (co.getChargeType() != null && mapChargeType.size() > 0) {
                    co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
                }
                if (co.getMeterAttr() != null && meterAttrType.size() > 0) {
                    co.put("meterAttrName", String.valueOf(meterAttrType.get(String.valueOf(co.getMeterAttr()))));
                }
                if (co.getTerm() != null && termType.size() > 0) {
                    co.put("termName", String.valueOf(termType.get(String.valueOf(co.getTerm()))));
                }
                byId.put("waterMeter", co);
            }
            String innerCode = byId.getInnerCode();
            if (StringUtils.isNotEmpty(innerCode)) {
                Company byInnerCode = Company.me.findByInnerCode(innerCode);
                if (byInnerCode != null) {
                    Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
                    Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
                    Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
                    Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
                    if (byInnerCode.getCustomerType() != null && mapUserType.size() > 0) {
                        byInnerCode.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(byInnerCode.getCustomerType()))));
                    }
                    if (byInnerCode.getWaterUseType() != null && mapWaterUseType.size() > 0) {
                        byInnerCode.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(byInnerCode.getWaterUseType()))));
                    }
                    if (byInnerCode.getUnitType() != null && mapUintType.size() > 0) {
                        byInnerCode.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(byInnerCode.getUnitType()))));
                    }
                    if (byInnerCode.getStreet() != null && mapStreetType.size() > 0) {
                        byInnerCode.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(byInnerCode.getStreet()))));
                    }
                    if (byInnerCode.getTerm() != null && termType.size() > 0) {
                        byInnerCode.put("termName", String.valueOf(termType.get(String.valueOf(byInnerCode.getTerm()))));
                    }
                }
                byId.put("company", byInnerCode);
            }
        }
        renderJson(byId);
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
        if (ActualState.Actual_List().contains(status)) {
            pageInfo = ActualDataWx.me.getWxActualDataPageByStatus(getPage(), this.getRows(), keyword, this.getOrderbyStr(), status, exceptionTime, wxInnerCodeSQLStr);
        } else if (ActualState.DISABLE.equals(status)) {
            pageInfo = ActualDataWx.me.getWxActualDataPageByDisable(getPage(), this.getRows(), keyword, this.getOrderbyStr(), wxInnerCodeSQLStr);
        } else {
            pageInfo = ActualDataWx.me.getWxActualDataPage(getPage(), this.getRows(), keyword, this.getOrderbyStr(), wxInnerCodeSQLStr);
        }
        List<ActualData> list = pageInfo.getList();
        setVoProp(list, exceptionTime);
        Page<ActualData> pageInfoFinal = new Page<ActualData>(list, pageInfo.getPageNumber(), pageInfo.getPageSize(), pageInfo.getTotalPage(), pageInfo.getTotalRow());
        this.renderJson(pageInfoFinal);
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
        Page<ActualData> pageInfo = ActualDataWx.me.findWxDailyList(getPage(), this.getRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void monthList() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxMonthList(getPage(), this.getRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void yearList() {
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxYearList(getPage(), this.getRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
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

            Page<ActualData> pageInfo = ActualDataWx.me.findWxYearList(getPage(), this.getWxRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
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

            Page<ActualData> pageInfo = ActualDataWx.me.findWxMonthList(getPage(), this.getWxRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
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

            Page<ActualData> pageInfo = ActualDataWx.me.findWxDailyList(getPage(), this.getWxRows(), this.getOrderbyStr(), startTime, endTime, keyword, wxInnerCodeSQLStr);
            obj.put("jsonList", pageInfo);
            this.renderJson(obj);
        } else {
            this.renderJson(InvokeResult.failure("错误日期类型"));
        }
    }
}
