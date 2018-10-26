package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataWx;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.WaterMeter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/10/26 12:26
 * @Desc 用途：
 */
public class ActualMeterReadController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void daily() {
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxMeterDailyActualData(wxInnerCodeSQLStr, meterAddress, startTime, endTime);
        WaterMeter byMeterAddress = WaterMeter.me.findByMeterAddress(meterAddress);
        Company byInnerCode = null;
        if (byMeterAddress != null && StringUtils.isNotEmpty(byMeterAddress.getInnerCode())) {
            byInnerCode = Company.me.findByInnerCode(byMeterAddress.getInnerCode());
        }
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
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxMeterMonthActualData(wxInnerCodeSQLStr, meterAddress, startTime, endTime);
        WaterMeter byMeterAddress = WaterMeter.me.findByMeterAddress(meterAddress);
        Company byInnerCode = null;
        if (byMeterAddress != null && StringUtils.isNotEmpty(byMeterAddress.getInnerCode())) {
            byInnerCode = Company.me.findByInnerCode(byMeterAddress.getInnerCode());
        }
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
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        List<Record> records = ActualDataWx.me.getWxMeterYearActualData(wxInnerCodeSQLStr, meterAddress, startTime, endTime);
        WaterMeter byMeterAddress = WaterMeter.me.findByMeterAddress(meterAddress);
        Company byInnerCode = null;
        if (byMeterAddress != null && StringUtils.isNotEmpty(byMeterAddress.getInnerCode())) {
            byInnerCode = Company.me.findByInnerCode(byMeterAddress.getInnerCode());
        }
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
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxMeterDailyList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr, meterAddress);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void monthList() {
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxMeterMonthList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr, meterAddress);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void yearList() {
        String meterAddress = this.getPara("meterAddress");
        String startTime = this.getPara("startTime");
        String endTime = this.getPara("endTime");
        String wxInnerCodeSQLStr = getWxInnerCodeSQLStr();
        String keyword = this.getPara("keyword");
        Page<ActualData> pageInfo = ActualDataWx.me.findWxMeterYearList(getPage(), this.getRows(), startTime, endTime, keyword, wxInnerCodeSQLStr, meterAddress);
        this.renderJson(pageInfo);
    }
}