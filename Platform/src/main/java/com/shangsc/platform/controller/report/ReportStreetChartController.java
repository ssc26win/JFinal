package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
        String globalInnerCode = getInnerCodesSQLStr();
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
        this.setAttr("startTime", this.getPara("startTime"));
        this.setAttr("endTime", this.getPara("endTime"));
        this.setAttr("street", street);
        this.setAttr("watersType", watersType);
        this.setAttr("meterAttr", meterAttr);
        this.setAttr("type", type);

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
                "(select tc.name,tc.inner_code,tc.company_type,tc.real_code,tc.street,tad.net_water,tad.write_time,tad.meter_address,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                " and lsall.street<>'' and lsall.street is not null  and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                (street != null ? " and lsall.street=" + street : "") +
                (StringUtils.isNotEmpty(type) ? " and lsall.company_type=" + type : "") +
                (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
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
                "(select tc.name,tc.inner_code,tc.company_type,tc.real_code,tc.street,tad.net_water,tad.write_time,tad.meter_address,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                " and lsall.street<>'' and lsall.street is not null  and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                (street != null ? " and lsall.street=" + street : "") +
                (StringUtils.isNotEmpty(type) ? " and lsall.company_type=" + type : "") +
                (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
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

        JSONArray meterAttrSeris = new JSONArray();

        String sqlMeterAttr = "select lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                "(select tc.name,tc.inner_code,tc.company_type,tc.real_code,tc.street,tad.net_water,tad.write_time,tad.meter_address,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                " and lsall.street<>'' and lsall.street is not null and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                (street != null ? " and lsall.street=" + street : "") +
                (StringUtils.isNotEmpty(type) ? " and lsall.company_type=" + type : "") +
                (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                " group by lsall.meter_attr order by lsall.meter_attr asc";
        List<Record> recordsMeterAttr = Db.find(sqlMeterAttr);

        Set<Integer> meterAttrs = new LinkedHashSet<>();
        for (Record record : recordsMeterAttr) {
            if (record.getInt("meter_attr") != null) {
                meterAttrs.add(record.getInt("meter_attr"));
            }
        }
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONArray meterAttrName = new JSONArray();
        for (String key : meterAttrType.keySet()) {
            if (meterAttrType.get(key) != null && meterAttrs.contains(Integer.parseInt(key))) {
                for (Integer mA : meterAttrs) {
                    if (key.equals(mA.toString())) {
                        meterAttrName.add(meterAttrType.get(key).toString());
                    }
                }
            }
        }
        this.setAttr("meterAttrName", meterAttrName);

        for (int i = 0; i < recordsMeterAttr.size(); i++) {
            Integer meter_attr = recordsMeterAttr.get(i).getInt("meter_attr");
            BigDecimal targetAttrTotal = recordsMeterAttr.get(i).getBigDecimal("TargetAttrTotal");
            if (targetAttrTotal != null && meterAttrs.contains(meter_attr)) {
                for (Integer mA : meterAttrs) {
                    if (meter_attr == mA.intValue()) {
                        meterAttrSeris.add(targetAttrTotal);
                    }
                }
            }
        }
        this.setAttr("meterAttrSeris", meterAttrSeris);

        Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

        JSONArray watersTypeSeris = new JSONArray();

        String sqlWatersType = "select COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal,lsall.waters_type from " +
                "(select tc.name,tc.inner_code,tc.company_type,tc.real_code,tc.street,tad.net_water,tad.write_time,tad.meter_address,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                " and lsall.street<>'' and lsall.street is not null  and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                (street != null ? " and lsall.street=" + street : "") +
                (StringUtils.isNotEmpty(type) ? " and lsall.company_type=" + type : "") +
                (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
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

        String url = contextPath + "/report/street?watersType=";
        //if (StringUtils.isNotEmpty(this.getPara("startTime"))) {
        //    url = url + "&startTime=" + this.getPara("startTime");
        //}
        //if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
        //    url = url + "&endTime=" + this.getPara("endTime");
        //}
        //if (street != null) {
        //    url = url + "&street=" + street;
        //}
        //if (watersType != null) {
        //    url = url + "&watersType=" + watersType;
        //}
        //if (meterAttr != null) {
        //    url = url + "&meterAttr=" + meterAttr;
        //}
        //if (StringUtils.isNotEmpty(type)) {
        //    url = url + "&type=" + type;
        //}

        for (int i = 0; i < recordsWatersType.size(); i++) {
            Record record = recordsWatersType.get(i);
            Object waters_type = record.get("waters_type");
            if (waters_type == null) {
                waters_type = "";
            }
            if (i == 0) {
                JSONObject data = new JSONObject();
                data.put("name", record.getStr("watersTypeName"));
                data.put("y", record.getBigDecimal("TargetAttrTotal"));
                data.put("url", url + waters_type);
                data.put("sliced", true);
                data.put("selected", true);
                watersTypeSeris.add(data);
            } else {
                JSONObject data = new JSONObject();
                data.put("name", record.getStr("watersTypeName"));
                data.put("y", record.getBigDecimal("TargetAttrTotal"));
                data.put("url", url + waters_type);
                data.put("sliced", false);
                data.put("selected", false);
                watersTypeSeris.add(data);
            }
        }
        this.setAttr("watersTypeSeris", watersTypeSeris);
        render("street_chart.jsp");
    }
}
