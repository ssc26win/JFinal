package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.ReportColType;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/10/11 15:44
 * @Desc 用途：
 */
public class ReportCompanyChartController extends BaseController {

    @RequiresPermissions(value = {"/report/company/chart"})
    public void index() {
        String path = this.getRequest().getServletContext().getContextPath();
        String contextPath = path.equals("/") ? "" : path;

        List<ActualData> datas = ActualData.me.find("select date_format(write_time, '%Y') as yearTime from t_actual_data" +
                " group by yearTime order by yearTime asc");
        String streetTitle = "各单位用水统计图表";
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

        String sqlSeries = "select lsall.name,lsall.inner_code,sum(lsall.net_water) as TargetAttrTotal from " +
                "(select tc.name,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where lsall.inner_code<>'' and lsall.inner_code is not null " +
                " group by lsall.inner_code order by lsall.inner_code asc";

        List<Record> recordsSeries = Db.find(sqlSeries);

        JSONArray seriesJsonData = new JSONArray();
        for (Record record : recordsSeries) {
            JSONObject data = new JSONObject();
            data.put("name", record.getStr("name"));
            data.put("y", record.getBigDecimal("TargetAttrTotal"));
            data.put("drilldown", record.getStr("name"));
            seriesJsonData.add(data);
        }

        this.setAttr("seriesJsonData", seriesJsonData);

        //drilldownJsonData

        String sqlSeriesMeter = "select lsall.meter_address,lsall.street,lsall.name,lsall.inner_code,sum(lsall.net_water) as TargetAttrTotal from " +
                "(select tc.street,tc.name,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr,twm.meter_address from t_actual_data tad " +
                " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                " where lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                " group by lsall.meter_address order by lsall.meter_address asc";

        List<Record> recordsSeriesMeter = Db.find(sqlSeriesMeter);


        JSONArray drilldownJsonData = new JSONArray();

        for (Record record : recordsSeries) {
            JSONObject data = new JSONObject();
            data.put("name", record.getStr("name"));
            data.put("id", record.getStr("name"));
            JSONArray cpyArray = new JSONArray();
            for (Record recordCpy : recordsSeriesMeter) {
                if (record.getStr("inner_code").equals(recordCpy.getStr("inner_code"))) {
                    JSONArray cpyArrayVal = new JSONArray();
                    cpyArrayVal.add(recordCpy.getStr("meter_address"));
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

        Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

        JSONArray watersTypeSeris = new JSONArray();

        String sqlWatersType = "select sum(lsall.net_water) as TargetAttrTotal,lsall.waters_type from " +
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
                data.put("url", contextPath + "/#/report/company");
                data.put("sliced", true);
                data.put("selected", true);
                watersTypeSeris.add(data);
            } else {
                JSONObject data = new JSONObject();
                data.put("name", record.getStr("watersTypeName"));
                data.put("y", record.getBigDecimal("TargetAttrTotal"));
                data.put("url", contextPath + "/#/report/company");
                data.put("sliced", false);
                data.put("selected", false);
                watersTypeSeris.add(data);
            }
        }
        this.setAttr("watersTypeSeris", watersTypeSeris);
        render("company_chart.jsp");
    }
}
