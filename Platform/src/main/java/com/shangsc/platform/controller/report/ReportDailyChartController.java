package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/21 23:06
 * @Version 1.0.0
 * @Desc
 */
public class ReportDailyChartController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/report/daily/chart"})
    public void index() {
        String name = this.getUrlUtf8Para("name");
        String innerCode = this.getUrlUtf8Para("innerCode");
        Date startTime = null;
        Date endTime = null;
        try {
            if (StringUtils.isNotEmpty(this.getPara("startTime"))) {
                startTime = DateUtils.getDate(this.getPara("startTime") + " 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.getDate(this.getPara("endTime") + " 23:59:59", ToolDateTime.pattern_ymd_hms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
        Integer meterAttr = null;
        if (StringUtils.isNotEmpty(this.getPara("meterAttr"))) {
            String meterAttrStr = StringUtils.trim(this.getPara("meterAttr"));
            meterAttr = Integer.parseInt(meterAttrStr);
        }
        String type = this.getPara("type");

        String globalInnerCode = getInnerCodesSQLStr();
        ActualData.me.setGlobalInnerCode(globalInnerCode);
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        List<ActualData> datas = ActualData.me.find("select date_format(write_time, '%Y-%m-%d') as targetTime from t_actual_data " +
                " where write_time is not null and write_time<>'' " +
                " and write_time >= '" + start + "' " +
                " and write_time < '" + end + "' " +
                " group by targetTime order by targetTime asc");
        String companyTitle = " 各单位用水统计图表";
        if (CollectionUtils.isNotEmpty(datas)) {
            String strTime = "";
            if (datas.size() == 1) {
                strTime = datas.get(0).get("targetTime");
            } else {
                strTime = datas.get(0).get("targetTime") + "—" + datas.get(datas.size() - 1).get("targetTime");
            }
            companyTitle = strTime + companyTitle;
        }
        this.setAttr("companyTitle", companyTitle);
        List<Record> recordsSeries = ActualData.me.getCPADailyActualData();
        JSONArray seriesJsonData = new JSONArray();
        for (Record record : recordsSeries) {
            JSONObject data = new JSONObject();
            data.put("name", record.getStr("DAY"));
            data.put("y", record.getBigDecimal("sumWater"));
            data.put("drilldown", record.getStr("DAY"));
            seriesJsonData.add(data);
        }

        this.setAttr("seriesJsonData", JsonUtil.obj2Json(seriesJsonData));

        render("daily_report_chart.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/report/daily/chart"})
    public void setOneDaily() {
        String name = this.getUrlUtf8Para("name");
        String innerCode = this.getUrlUtf8Para("innerCode");
        Date startTime = null;
        Date endTime = null;
        try {
            if (StringUtils.isNotEmpty(this.getPara("startTime"))) {
                startTime = DateUtils.getDate(this.getPara("startTime") + " 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.getDate(this.getPara("endTime") + " 23:59:59", ToolDateTime.pattern_ymd_hms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
        Integer meterAttr = null;
        if (StringUtils.isNotEmpty(this.getPara("meterAttr"))) {
            String meterAttrStr = StringUtils.trim(this.getPara("meterAttr"));
            meterAttr = Integer.parseInt(meterAttrStr);
        }
        String type = this.getPara("type");

        String globalInnerCode = getInnerCodesSQLStr();
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String date = this.getPara("date");
        String sqlSeriesDay = "select sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.*,tc.name from t_actual_data t " +
                " inner join (select name,inner_code from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " t.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                " and t.write_time >= '" + start + "' " +
                " and t.write_time < '" + end + "' " +
                " and date_format(t.write_time, '%Y-%m-%d') = '" + date + "' " +
                " group by t.inner_code";

        List<Record> recordsSeriesCompany = Db.find(sqlSeriesDay);

        List<String> companies = new ArrayList<String>();
        JSONArray sumWater = new JSONArray();
        for (Record record : recordsSeriesCompany) {
            sumWater.add(record.get("sumWater"));
            companies.add(record.get("name").toString());
        }

        JSONObject obj = new JSONObject();
        obj.put("sumWater", sumWater);
        obj.put("companies", companies);
        this.renderJson(obj);
    }

}
