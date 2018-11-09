package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.ReportColType;
import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ExportByDataTypeService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataReport;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/21 23:11
 * @Version 1.0.0
 * @Desc
 */
public class ReportStreetChartController extends BaseController {

    @RequiresPermissions(value = {"/report/street/chart"})
    public void index() {
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

        String path = this.getRequest().getServletContext().getContextPath();
        String contextPath = path.equals("/") ? "" : path;

        List<ActualData> datas = ActualData.me.find("select date_format(write_time, '%Y') as yearTime from t_actual_data" +
                " group by yearTime order by yearTime asc");
        String streetTitle = "各乡镇用水统计图表";
        String meterAttrTitle = "根据水表属性用水量统计图表";
        String watersTypeTitle = "根据水源类型统计用水量占比";
        if (CollectionUtils.isNotEmpty(datas)) {
            String strYear = "";
            if (datas.size() == 1) {
                strYear = datas.get(0).get("yearTime");
            } else {
                strYear = datas.get(0).get("yearTime") + "—" + datas.get(datas.size() - 1).get("yearTime");
            }
            streetTitle = strYear + " 年 " + streetTitle;
            meterAttrTitle = strYear + " 年 " + meterAttrTitle;
            watersTypeTitle = strYear + " 年 " + watersTypeTitle;
        }
        this.setAttr("streetTitle", streetTitle);
        this.setAttr("meterAttrTitle", meterAttrTitle);
        this.setAttr("watersTypeTitle", watersTypeTitle);

        //seriesJsonData
        Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);

        String sqlSeries = "select lsall.street,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                "(select tc.street,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where lsall.street<>'' and lsall.street is not null " +
                " group by lsall.street order by lsall.street asc";

        List<Record> recordsSeries = Db.find(sqlSeries);


        for (int i = 0; i < recordsSeries.size(); i++) {
            Record record = recordsSeries.get(i);
            if (record.get("street") != null) {
                record.set("streetName", String.valueOf(mapStreetType.get(String.valueOf(record.get("street")))));
            }
            recordsSeries.set(i, record);
        }

        JSONArray seriesJsonData = new JSONArray();
        for (Record record : recordsSeries) {
            JSONObject data = new JSONObject();
            data.put("name", record.getStr("streetName"));
            data.put("y", record.getBigDecimal("TargetAttrTotal"));
            data.put("drilldown", record.getStr("streetName"));
            seriesJsonData.add(data);
        }

        this.setAttr("seriesJsonData", seriesJsonData);

        //drilldownJsonData

        String sqlSeriesCpy = "select lsall.street,lsall.name,lsall.inner_code,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                "(select tc.street,tc.name,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where lsall.inner_code<>'' and lsall.inner_code is not null and lsall.street<>'' and lsall.street is not null " +
                " group by lsall.inner_code order by lsall.inner_code asc";

        List<Record> recordsSeriesCpy = Db.find(sqlSeriesCpy);


        JSONArray drilldownJsonData = new JSONArray();

        for (Record record : recordsSeries) {
            JSONObject data = new JSONObject();
            data.put("name", record.getStr("streetName"));
            data.put("id", record.getStr("streetName"));
            JSONArray cpyArray = new JSONArray();
            for (Record recordCpy : recordsSeriesCpy) {
                if (record.getInt("street") == recordCpy.getInt("street").intValue()) {
                    JSONArray cpyArrayVal = new JSONArray();
                    cpyArrayVal.add(recordCpy.getStr("name"));
                    cpyArrayVal.add(recordCpy.getBigDecimal("TargetAttrTotal"));
                    cpyArray.add(cpyArrayVal);
                }
            }
            data.put("data", cpyArray);
            drilldownJsonData.add(data);
        }

        this.setAttr("drilldownJsonData", drilldownJsonData);

        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONArray meterAttrName = new JSONArray();
        for (String key : meterAttrType.keySet()) {
            if (meterAttrType.get(key) != null) {
                meterAttrName.add(meterAttrType.get(key).toString());
            }
        }
        this.setAttr("meterAttrName", meterAttrName);

        JSONArray meterAttrSeris = new JSONArray();

        String sqlMeterAttr = "select COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                "(select tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr,tad.write_time from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where 1=1 " +
                " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                " group by lsall.meter_attr order by lsall.write_time asc";
        List<Record> recordsMeterAttr = Db.find(sqlMeterAttr);

        for (int i = 0; i < recordsMeterAttr.size(); i++) {
            if (recordsMeterAttr.get(i).getBigDecimal("TargetAttrTotal") != null) {
                meterAttrSeris.add(recordsMeterAttr.get(i).getBigDecimal("TargetAttrTotal"));
            }
        }
        this.setAttr("meterAttrSeris", meterAttrSeris);

        Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

        JSONArray watersTypeSeris = new JSONArray();

        String sqlWatersType = "select COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal,lsall.waters_type from " +
                "(select tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr,tad.write_time from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where 1=1 " +
                //" and lsall.waters_type<>'' and lsall.waters_type is not null " +
                " group by lsall.waters_type order by TargetAttrTotal desc";
        List<Record> recordsWatersType = Db.find(sqlWatersType);

        for (int i = 0; i < recordsWatersType.size(); i++) {
            Record record = recordsWatersType.get(i);
            if (record.get("waters_type") != null) {
                record.set("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(record.get("waters_type")))));
            } else {
                record.set("watersTypeName", "其他");
            }
            recordsWatersType.set(i, record);
        }


        for (int i = 0; i < recordsWatersType.size(); i++) {
            Record record = recordsWatersType.get(i);
            Object waters_type = record.get("waters_type");
            if (waters_type == null) {
                waters_type = "0";
            }
            if (i == 0) {
                JSONObject data = new JSONObject();
                data.put("name", record.getStr("watersTypeName"));
                data.put("y", record.getBigDecimal("TargetAttrTotal"));
                data.put("url", contextPath + "/#/report/street");
                data.put("sliced", true);
                data.put("selected", true);
                watersTypeSeris.add(data);
            } else {
                JSONObject data = new JSONObject();
                data.put("name", record.getStr("watersTypeName"));
                data.put("y", record.getBigDecimal("TargetAttrTotal"));
                data.put("url", contextPath + "/#/report/street");
                data.put("sliced", false);
                data.put("selected", false);
                watersTypeSeris.add(data);
            }
        }
        this.setAttr("watersTypeSeris", watersTypeSeris);
        render("street_chart.jsp");
    }
}
