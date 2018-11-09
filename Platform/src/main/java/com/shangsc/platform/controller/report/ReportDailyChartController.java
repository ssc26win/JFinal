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
import com.shangsc.platform.model.ActualDataReport;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

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

                (startTime != null ? " and write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and write_time >='" + start + "'") +
                (endTime != null ? " and write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and write_time <='" + end + "'") +

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
        List<Record> recordsSeries = ActualDataReport.me.getCPADailyActualData(name, innerCode, street, startTime, endTime, watersType, meterAttr, type);
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
        String sqlSeriesDay = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.*,tc.name from t_actual_data t " +
                " inner join (select name,inner_code,real_code,street,company_type from t_company) tc on tc.inner_code=t.inner_code " +
                " inner join (select waters_type,meter_attr,meter_address from t_water_meter) twm on twm.meter_address=t.meter_address " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " t.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(name) ? " and tc.name='" + name + "'" : "") +
                (StringUtils.isNotEmpty(innerCode) ? " and tc.inner_code='" + innerCode + "'" : "") +
                (street != null ? " and tc.street=" + street : "") +
                (startTime != null ? " and t.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time >='" + start + "'") +
                (endTime != null ? " and t.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time <='" + end + "'") +
                (StringUtils.isNotEmpty(type) ? " and tc.company_type=" + type : "") +
                (meterAttr != null ? " and twm.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and twm.waters_type=" + watersType : "") +
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

        //logger.info("--【日用水量明细】 -- \n{}" , JsonUtil.obj2Json(obj));

        this.renderJson(obj);
    }

}
