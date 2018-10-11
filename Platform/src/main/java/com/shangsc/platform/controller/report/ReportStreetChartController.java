package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.ReportColType;
import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ExportByDataTypeService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataReport;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
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
        List<ActualData> datas = ActualData.me.find("select date_format(write_time, '%Y') as yearTime from t_actual_data group by yearTime order by yearTime asc");
        String streetTitle = "各乡镇用水统计图表";
        String meterAttrTitle = "按水表属性用水量统计图表";
        if (CollectionUtils.isNotEmpty(datas)) {
            String strYear = "";
            if (datas.size() == 1) {
                strYear = datas.get(0).get("yearTime");
            } else {
                strYear = datas.get(0).get("yearTime") + "—" + datas.get(datas.size() - 1).get("yearTime");
            }
            streetTitle = strYear + " 年 " + streetTitle;
            meterAttrTitle = strYear + " 年 " + meterAttrTitle;
        }
        this.setAttr("streetTitle", streetTitle);
        this.setAttr("meterAttrTitle", meterAttrTitle);

        //seriesJsonData
        Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);

        String sqlSeries = "select lsall.street,sum(lsall.net_water) as TargetAttrTotal from " +
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

        String sqlSeriesCpy = "select lsall.street,lsall.name,lsall.inner_code,sum(lsall.net_water) as TargetAttrTotal from " +
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


        /**
         *
         */
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONArray meterAttrName = new JSONArray();
        for (String key : meterAttrType.keySet()) {
            if (meterAttrType.get(key) != null) {
                meterAttrName.add(meterAttrType.get(key).toString());
            }
        }
        this.setAttr("meterAttrName", meterAttrName);

        JSONArray meterAttrSeris = new JSONArray();

        String sqlMeterAttr = "select sum(lsall.net_water) as TargetAttrTotal from " +
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
        render("street_chart.jsp");
    }

    @RequiresPermissions(value = {"/report/street/chart"})
    public void columnar() {

        renderJson("");

    }
}
