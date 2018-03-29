package com.shangsc.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MapState;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.*;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/8/26.
 */
public class ChartController extends BaseController {
    // 预警閥值
    private static final BigDecimal THRESHOLD = new BigDecimal("2");

    private AtomicInteger count = new AtomicInteger(0);
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void company() {
        JSONObject object = new JSONObject();
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("total", total);//单位总数
        // 预警或报警用水单位
        Set<String> warnOrExceptionCompanies = Company.me.getWarnExceptionInnerCode(new Date());
        int warnOrExceptionCount = 0;
        int supplyCount = Company.me.getSupplyCompanyCount();
        if (CollectionUtils.isNotEmpty(warnOrExceptionCompanies)) {
            warnOrExceptionCount = warnOrExceptionCompanies.size();
        }
        int normalTotal = total-warnOrExceptionCount-supplyCount;

        object.put("warnTotal", warnOrExceptionCount); //预警总数

        object.put("normalTotal", normalTotal);

        object.put("supplyTotal", supplyCount);

        int month = new Date().getMonth()+1;

        if (CodeNumUtil.isOdd(month)) {
            object.put("warnTitle", "预警");
        } else {
            object.put("warnTitle", "告警");
        }
        this.renderJson(object.toJSONString());
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void index() {
        JSONObject object = new JSONObject();
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();
        object.put("total", total);
        // 异常水表数量24小时之内都会接收到数据 否则就异常
        int normalTotal = ActualData.me.getNormalMeter().size();//正常水表
        int stopTotal = ActualData.me.getStopMeter().size();//停用水表
        int disableTotal = ActualData.me.getDisableMeter().size();//未启用水表

        object.put("normalTotal", normalTotal);
        object.put("stopTotal", stopTotal);
        object.put("disableTotal", disableTotal);
        object.put("exptionTotal", total-normalTotal-stopTotal-disableTotal);//异常水表

        this.renderJson(object.toJSONString());
    }

    @Deprecated
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void companyBak() {
        JSONObject object = new JSONObject();
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("total", total);//单位总数
        List<WaterIndex> waterIndices = WaterIndex.me.getAllList();
        for (WaterIndex index : waterIndices) {
            index.getWaterIndex();//年
            WaterMeter waterMeter = WaterMeter.me.findByInnerCode(index.getInnerCode());
            if (null != waterMeter) {
                Record records1 = ActualData.me.getYearActual(index.getInnerCode());
                if (null != records1) {
                    if (records1.get("yearTotal") != null) {
                        comp((BigDecimal) records1.get("yearTotal"), (BigDecimal) index.getWaterIndex());
                    }
                    List<Record> records = ActualData.me.getMonthActualDataPage(index.getInnerCode());
                    for (int i = 0; i < records.size(); i++) {
                        Record record = records.get(i);
                        BigDecimal monthActTotal = new BigDecimal(record.get("total").toString());
                        switch ((record.get("time").toString())) {
                            case "01":
                                comp(monthActTotal, index.getJanuary());
                                break;
                            case "02":
                                comp(monthActTotal, index.getFebruary());
                                break;
                            case "03":
                                comp(monthActTotal, index.getMarch());
                                break;
                            case "04":
                                comp(monthActTotal, index.getApril());
                                break;
                            case "05":
                                comp(monthActTotal, index.getMay());
                                break;
                            case "06":
                                comp(monthActTotal, index.getJune());
                                break;
                            case "07":
                                comp(monthActTotal, index.getJuly());
                                break;
                            case "08":
                                comp(monthActTotal, index.getAugust());
                                break;
                            case "09":
                                comp(monthActTotal, index.getSeptember());
                                break;
                            case "10":
                                comp(monthActTotal, index.getOctober());
                                break;
                            case "11":
                                comp(monthActTotal, index.getNovember());
                                break;
                            case "12":
                                comp(monthActTotal, index.getDecember());
                                break;
                        }
                    }
                }
            }
        }
        // 正常用水单位
        int normalTotal = Company.me.hasActual();
        object.put("normalTotal", normalTotal-count.get());
        object.put("warnTotal", count); //预警总数
        object.put("otherTotal", total-normalTotal);
        object.put("supplyTotal", Company.me.getSupplyCompanyCount());
        this.renderJson(object.toJSONString());
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getDilay() {
        List<Record> records = ActualData.me.getDailyActualData();
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add( record.get("DAY").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("day", day);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getMonth() {
        List<Record> records = ActualData.me.getMonthActualData();
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> month = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            month.add( record.get("month").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("month", month);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getSupplyDay() {
        List<Record> records = ActualData.me.getSupplyDailyActualData();
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add( record.get("DAY").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("day", day);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getSupplyMonth() {
        List<Record> records = ActualData.me.getSupplyMonthActualData();
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add( record.get("month").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("month", day);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/chart"})
    public void baiduMap() {
        Company.me.setGlobalInnerCode(getInnerCode());
        List<Record> records = new ArrayList<>();
        JSONArray array = new JSONArray();
        String type = this.getPara("type");
        if (StringUtils.isNotEmpty(type)) {
            records = Company.me.getCompanyByType(type);
            this.setAttr("type", type);
        } else {
            String innerCode = this.getPara("innerCode");
            records = Company.me.getCompanyAll(innerCode);
        }
        Date date = new Date();
        Set<String> warnCodes = Company.me.getWarnExceptionInnerCode(date);
        int month = new Date().getMonth()+1;
        for (Record record:records) {
            String innerCode = record.get("inner_code").toString();
            JSONObject object = new JSONObject();
            object.put("longitude", record.get("longitude"));
            object.put("latitude", record.get("latitude"));
            object.put("innerCode", record.get("inner_code"));
            object.put("state", MapState.NORMAL);
            if (warnCodes.contains(innerCode)) {
                if (CodeNumUtil.isOdd(month)) {//预警
                    object.put("state", MapState.WARN);
                } else {//告警
                    object.put("state", MapState.OVER);
                }
            }
            object.put("name", record.get("name"));
            if (record.get("waterUseNum")!=null) {
                object.put("waterUseNum", record.get("waterUseNum"));
            } else {
                object.put("waterUseNum", 0);
            }
            if ("0".equals(object.get("waterUseNum").toString())) {
                object.put("waterUseNum", 0);
            }
            object.put("address", record.get("address"));
            array.add(object);
        }
        if (CollectionUtils.isNotEmpty(records) && records.size() == 1) {
            String centerPosition = records.get(0).get("longitude") + "," + records.get(0).get("latitude");
            this.setAttr("position", centerPosition);
        }
        this.setAttr("companys", array.toJSONString());
        render("map.jsp");
    }

    private void comp(BigDecimal monthActTotal, BigDecimal moth) {
        if (null == moth) {
            moth = new BigDecimal(0);
        }
        if (moth.add(THRESHOLD).compareTo(monthActTotal) < 0) {
            count.addAndGet(1);
        }
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getNewsMsg() {
        Ad ad = Ad.dao.findFirst("select * from t_ad where status=1");
        JSONObject obj = new JSONObject();
        obj.put("title", ad.getTitle());
        obj.put("content", ad.getContent());
        this.renderJson(obj);
    }
}
