package com.shangsc.platform.controller.chart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/10/9 18:06
 * @Desc 用途：
 */
public class CompanyWaterController extends BaseController {

    @RequiresPermissions(value = {"/chart/companyWaterLine"})
    public void index() {
        render("company_line_chart.jsp");
    }

    @RequiresPermissions(value = {"/chart/companyWaterLine"})
    public void getCPADailyChart() {
        ActualData.me.setGlobalInnerCode(getInnerCodesSQLStr());
        List<Record> records = ActualData.me.getCPADailyActualData();
        Company byInnerCode = Company.me.findByInnerCode(getInnerCodesSQLStr());
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

    @RequiresPermissions(value = {"/chart/companyWaterLine"})
    public void getCPAMonth() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        List<Record> records = ActualData.me.getCPAMonthActualData();
        Company byInnerCode = Company.me.findByInnerCode(getInnerCode());
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
}
