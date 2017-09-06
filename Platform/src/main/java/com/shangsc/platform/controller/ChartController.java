package com.shangsc.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.WaterIndex;
import com.shangsc.platform.model.WaterMeter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/8/26.
 */
public class ChartController extends Controller {

    // 预警閥值
    private static final BigDecimal THRESHOLD = new BigDecimal("2");

    private AtomicInteger count = new AtomicInteger(0);

    @RequiresPermissions(value = {"/chart"})
    public void index() {
        JSONObject object = new JSONObject();
        // 水表总数量
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();

        object.put("total", total);

        // 异常水表数量 水表每天都会接收到数据 否则就异常
        int today = ActualData.me.getToddayActualDataPage().size();

        object.put("exptionTotal", (total >= today) ? (total - today) : 0);

        object.put("normalTotal", today);

        this.renderJson(object.toJSONString());
    }

    @RequiresPermissions(value = {"/chart"})
    public void company() {
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
                    comp((BigDecimal) records1.get("yearTotal"), (BigDecimal) index.getWaterIndex());

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
        this.renderJson(object.toJSONString());
    }

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

    @RequiresPermissions(value={"/chart"})
    public void baiduMap() {
        String innerCode = this.getPara("innerCode");
        JSONArray array = new JSONArray();
        List<Record> records = Company.me.getCompanyAll(innerCode);
        for (Record record:records) {
            JSONObject object = new JSONObject();
            object.put("longitude", record.get("longitude"));
            object.put("latitude", record.get("latitude"));
            object.put("innerCode", record.get("inner_code"));
            object.put("name", record.get("name"));
            if (record.get("waterUseNum")!=null) {
                object.put("waterUseNum", record.get("waterUseNum"));
            } else {
                object.put("waterUseNum", 0);
            }
            object.put("address", record.get("address"));
            array.add(object);
        }
        this.setAttr("companys", array.toJSONString());
        render("map.jsp");
    }

    private void comp(BigDecimal monthActTotal, BigDecimal moth) {
        if (null == moth) {
            moth = new BigDecimal(0);
        }
        if (moth.add(this.THRESHOLD).compareTo(monthActTotal) < 0) {
            count.addAndGet(1);
        }
    }
}
