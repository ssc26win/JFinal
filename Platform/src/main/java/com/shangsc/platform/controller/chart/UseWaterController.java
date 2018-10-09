package com.shangsc.platform.controller.chart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/10/9 18:05
 * @Desc 用途：
 */
public class UseWaterController extends BaseController {

    @RequiresPermissions(value = {"/chart/useWaterLine"})
    public void index() {
        render("use_line_chart.jsp");
    }

    @RequiresPermissions(value = {"/chart/useWaterLine"})
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

    @RequiresPermissions(value = {"/chart/useWaterLine"})
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
}
