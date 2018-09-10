package com.shangsc.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.MapState;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.*;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/8/26.
 */
public class ChartController extends BaseController {
    
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void companiesByTerm() {
        JSONObject object = new JSONObject();
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("total", total);//单位总数

        Map<Integer, Object> termGroup = Company.me.getTermGroup();
        String contextPath = this.getRequest().getServletContext().getContextPath();
        String context_path = contextPath.equals("/") ? "" : contextPath;
        JSONArray array = new JSONArray();
        Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
        //{name:'一期' + '(' + stage1Total + ')', y:stage1Total,url:'${context_path}/basic/meter/byTerm?term=1'},
        for (Integer termKey : termGroup.keySet()) {
            JSONObject serObj = new JSONObject();
            serObj.put("name", termType.get(termKey.toString()) + "(" + termGroup.get(termKey).toString() + ")");
            serObj.put("y", (Long) termGroup.get(termKey));
            serObj.put("url", context_path + "/basic/company?term=" + termKey);
            array.add(serObj);
        }
        object.put("CompanyTermSerArray", array);
        this.renderJson(object);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void metersByTerm() {
        JSONObject object = new JSONObject();
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();
        object.put("total", total);
        Map<Integer, Object> termGroup = WaterMeter.me.getTermGroup();
        String contextPath = this.getRequest().getServletContext().getContextPath();
        String context_path = contextPath.equals("/") ? "" : contextPath;
        JSONArray array = new JSONArray();
        Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
        //{name:'一期' + '(' + stage1Total + ')', y:stage1Total,url:'${context_path}/basic/meter/byTerm?term=1'},
        for (Integer termKey : termGroup.keySet()) {
            JSONObject serObj = new JSONObject();
            serObj.put("name", termType.get(termKey.toString()) + "(" + termGroup.get(termKey).toString() + ")");
            serObj.put("y", (Long) termGroup.get(termKey));
            serObj.put("url", context_path + "/basic/meter?term=" + termKey);
            array.add(serObj);
        }
        object.put("MeterTermSerArray", array);
        this.renderJson(object);
    }

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
        int normalTotal = total - warnOrExceptionCount - supplyCount;

        object.put("warnTotal", warnOrExceptionCount); //预警总数
        object.put("normalTotal", normalTotal);
        object.put("supplyTotal", supplyCount);

        int month = new Date().getMonth() + 1;

        if (CodeNumUtil.isOdd(month)) {
            object.put("warnTitle", "预警");
        } else {
            object.put("warnTitle", "告警");
        }
        this.renderJson(object.toJSONString());
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void meter() {
        JSONObject object = new JSONObject();
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();
        object.put("total", total);
        // 异常水表数量24小时之内都会接收到数据 否则就异常
        List<Record> normalMeter = ActualData.me.getNormalMeter();
        Set<String> normalMeterSets = new HashSet<>();
        for (Record record : normalMeter) {
            String meter_address = record.getStr("meter_address");
            if (StringUtils.isNotEmpty(meter_address)) {
                normalMeterSets.add(meter_address);
            }
        }
        int normalTotal = ActualData.me.getNormalMeter().size();//正常水表
        List<Record> stopMeter = ActualData.me.getStopMeter();
        Set<String> stopMeterSets = new HashSet<>();
        for (Record record : stopMeter) {
            String meter_address = record.getStr("meter_address");
            if (StringUtils.isNotEmpty(meter_address) && !normalMeterSets.contains(meter_address)) {
                stopMeterSets.add(meter_address);
            }
        }
        int stopTotal = stopMeterSets.size();//停用水表
        int disableTotal = ActualData.me.getDisableMeter().size();//未启用水表

        object.put("normalTotal", normalTotal);
        object.put("stopTotal", stopTotal);
        object.put("disableTotal", disableTotal);
        object.put("exptionTotal", total - normalTotal - stopTotal - disableTotal);//异常水表

        this.renderJson(object.toJSONString());
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getDaily() {
        List<Record> records = ActualData.me.getDailyActualData();
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add(record.get("DAY").toString());
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
            month.add(record.get("month").toString());
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
            day.add(record.get("DAY").toString());
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
            day.add(record.get("month").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("month", day);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
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
        int month = new Date().getMonth() + 1;
        for (Record record : records) {
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
            if (record.get("waterUseNum") != null) {
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

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/chart"})
    public void getNewsMsg() {
        Ad ad = Ad.dao.findFirst("select * from t_ad where status=1");
        if (ad != null) {
            JSONObject obj = new JSONObject();
            obj.put("title", ad.getTitle());
            obj.put("content", ad.getContent());
            this.renderJson(obj);
        } else {
            renderJson(new Object());
        }
    }
}
