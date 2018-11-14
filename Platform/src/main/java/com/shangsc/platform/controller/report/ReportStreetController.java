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
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ExportByDataTypeService;
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
public class ReportStreetController extends BaseController {

    @RequiresPermissions(value = {"/report/street"})
    public void index() {
        JSONArray array = new JSONArray();
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONObject street = new JSONObject();
        street.put("label", "所属乡镇");
        street.put("name", "streetName");
        street.put("width", "120");
        street.put("sortable", false);
        array.add(street);
        JSONObject company = new JSONObject();
        company.put("label", "水源类型");
        company.put("name", "watersTypeName");
        company.put("width", "100");
        company.put("sortable", false);
        array.add(company);
        JSONObject waterUseTotal = new JSONObject();
        waterUseTotal.put("label", "总用水量");
        waterUseTotal.put("name", "watersUseTotal");
        waterUseTotal.put("width", "100");
        waterUseTotal.put("sortable", false);
        array.add(waterUseTotal);
        for (String value : meterAttrType.keySet()) {
            JSONObject column = new JSONObject();
            column.put("label", meterAttrType.get(value).toString());
            column.put("name", ReportColType.street_col + value);
            if (meterAttrType.get(value).toString().length() >= 3) {
                column.put("width", meterAttrType.get(value).toString().length() * 16);
            } else {
                column.put("width", 50);
            }
            column.put("sortable", false);
            array.add(column);
        }
        this.setAttr("columnsMeterAttr", array);
        this.setAttr("watersType", this.getPara("watersType"));
        render("street_report.jsp");
    }

    @RequiresPermissions(value = {"/report/street"})
    public void getListData() {
        String globalInnerCode = getInnerCodesSQLStr();
        ActualDataReport.me.setGlobalInnerCode(globalInnerCode);
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

        List<Company> listFinal = new ArrayList<>();

        Page<Company> pageInfo = ActualDataReport.me.getStreet(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), street, watersType, type);
        List<Company> list = pageInfo.getList();

        if (CollectionUtils.isNotEmpty(list)) {
            Set<Integer> streets = new HashSet<>();
            Set<Integer> watersTypes = new HashSet<>();
            for (Company company : list) {
                streets.add(company.getStreet());
                watersTypes.add(company.getInt("waters_type"));
            }

            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

            String sql = "select lsall.street,lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.street,lsall.waters_type,lsall.meter_attr order by lsall.street asc,lsall.waters_type asc";

            List<Record> records = Db.find(sql);

            Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);

            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getStreet() != null) {
                    company.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(company.getStreet()))));
                }
                if (company.get("waters_type") != null) {
                    company.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(company.get("waters_type")))));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");

                for (String colKey : meterAttrType.keySet()) {
                    company.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                Integer waterTypeTarget = company.getInt("waters_type");
                Integer streetTarget = company.getStreet();
                for (Record record : records) {
                    Integer waters_type = record.getInt("waters_type");
                    Integer street_t = record.getInt("street");
                    if (waterTypeTarget == waters_type.intValue() && streetTarget == street_t.intValue()) {
                        String colStr = record.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = record.getBigDecimal("TargetAttrTotal");
                        }
                        company.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                company.put("watersUseTotal", watersUseTotal);
                list.set(i, company);
            }

            String sqlZongji = "select lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsZongji = Db.find(sqlZongji);
            Company companyZongji = new Company();
            companyZongji.put("streetName", "合计");
            companyZongji.put("watersTypeName", "小计");
            for (String colKey : meterAttrType.keySet()) {
                companyZongji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
            }
            BigDecimal watersUseTotalAll = new BigDecimal("0");
            for (Record record : recordsZongji) {
                String colStr = record.getInt("meter_attr").toString();
                BigDecimal colVal = new BigDecimal("0");
                if (record.getBigDecimal("TargetAttrTotal") != null) {
                    colVal = record.getBigDecimal("TargetAttrTotal");
                }
                companyZongji.put(ReportColType.street_col + colStr, colVal);
                watersUseTotalAll = watersUseTotalAll.add(colVal);
            }
            companyZongji.put("watersUseTotal", watersUseTotalAll);
            listFinal.add(companyZongji); // TODO 1

            String sqlHejiWaterType = "select lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.waters_type,lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsHejiWaterType = Db.find(sqlHejiWaterType);

            List<Record> listWaterType = Db.find("select waters_type from t_water_meter where waters_type is not null " +
                    " and waters_type in (" + StringUtils.join(watersTypes, ",") + ") "+
                    " group by waters_type");

            for (int i = 0; i < listWaterType.size(); i++) {
                Record record = listWaterType.get(i);
                Integer waterTypeTarget = record.getInt("waters_type");
                Company companyHejiWaterType = new Company();
                companyHejiWaterType.put("streetName", "合计");
                if (record.get("waters_type") != null) {
                    companyHejiWaterType.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(record.get("waters_type")))));
                }
                for (String colKey : meterAttrType.keySet()) {
                    companyHejiWaterType.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record recordC : recordsHejiWaterType) {
                    Integer waters_type = recordC.getInt("waters_type");
                    if (waterTypeTarget == waters_type.intValue()) {
                        String colStr = recordC.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (recordC.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = recordC.getBigDecimal("TargetAttrTotal");
                        }
                        companyHejiWaterType.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                companyHejiWaterType.put("watersUseTotal", watersUseTotal);
                listFinal.add(companyHejiWaterType);//TODO 2
            }

            String sqlXiaoji = "select lsall.street,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.street,lsall.meter_attr order by lsall.street asc";

            List<Record> recordsXiaoji = Db.find(sqlXiaoji);

            List<Record> listStreet = Db.find("select street from t_company where street is not null group by street ");

            for (int i = 0; i < listStreet.size(); i++) {
                Record streetXiaoji = listStreet.get(i);
                Integer streetTarget = streetXiaoji.getInt("street");
                Company companyXiaoji = new Company();
                if (streetTarget != null) {
                    companyXiaoji.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(streetTarget))));
                }
                companyXiaoji.setStreet(streetTarget);
                companyXiaoji.put("watersTypeName", "小计");
                for (String colKey : meterAttrType.keySet()) {
                    companyXiaoji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record record : recordsXiaoji) {
                    Integer street_t = record.getInt("street");
                    if (streetTarget == street_t.intValue()) {
                        String colStr = record.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = record.getBigDecimal("TargetAttrTotal");
                        }
                        companyXiaoji.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                companyXiaoji.put("watersUseTotal", watersUseTotal);
                listFinal.add(companyXiaoji);
                for (Company company : list) {// TODO 3
                    if (streetTarget == company.getStreet().intValue()) {
                        listFinal.add(company);
                    }
                }
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, listFinal));
    }

    @RequiresPermissions(value = {"/report/street"})
    public void exportData() {
        String globalInnerCode = getInnerCodesSQLStr();
        ActualDataReport.me.setGlobalInnerCode(globalInnerCode);
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

        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);

        List<Company> listFinal = new ArrayList<>();

        Page<Company> pageInfo = ActualDataReport.me.getStreet(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), street, watersType, type);
        List<Company> list = pageInfo.getList();

        if (CollectionUtils.isNotEmpty(list)) {
            Set<Integer> streets = new HashSet<>();
            Set<Integer> watersTypes = new HashSet<>();
            for (Company company : list) {
                streets.add(company.getStreet());
                watersTypes.add(company.getInt("waters_type"));
            }

            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

            String sql = "select lsall.street,lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.street,lsall.waters_type,lsall.meter_attr order by lsall.street asc,lsall.waters_type asc";

            List<Record> records = Db.find(sql);

            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getStreet() != null) {
                    company.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(company.getStreet()))));
                }
                if (company.get("waters_type") != null) {
                    company.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(company.get("waters_type")))));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");

                for (String colKey : meterAttrType.keySet()) {
                    company.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                Integer waterTypeTarget = company.getInt("waters_type");
                Integer streetTarget = company.getStreet();
                for (Record record : records) {
                    Integer waters_type = record.getInt("waters_type");
                    Integer street_t = record.getInt("street");
                    if (waterTypeTarget == waters_type.intValue() && streetTarget == street_t.intValue()) {
                        String colStr = record.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = record.getBigDecimal("TargetAttrTotal");
                        }
                        company.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                company.put("watersUseTotal", watersUseTotal);
                list.set(i, company);
            }

            String sqlZongji = "select lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsZongji = Db.find(sqlZongji);
            Company companyZongji = new Company();
            companyZongji.put("streetName", "合计");
            companyZongji.put("watersTypeName", "小计");
            for (String colKey : meterAttrType.keySet()) {
                companyZongji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
            }
            BigDecimal watersUseTotalAll = new BigDecimal("0");
            for (Record record : recordsZongji) {
                String colStr = record.getInt("meter_attr").toString();
                BigDecimal colVal = new BigDecimal("0");
                if (record.getBigDecimal("TargetAttrTotal") != null) {
                    colVal = record.getBigDecimal("TargetAttrTotal");
                }
                companyZongji.put(ReportColType.street_col + colStr, colVal);
                watersUseTotalAll = watersUseTotalAll.add(colVal);
            }
            companyZongji.put("watersUseTotal", watersUseTotalAll);
            listFinal.add(companyZongji); // TODO 1


            String sqlHejiWaterType = "select lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.waters_type,lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsHejiWaterType = Db.find(sqlHejiWaterType);

            List<Record> listWaterType = Db.find("select waters_type from t_water_meter where waters_type is not null group by waters_type");

            for (int i = 0; i < listWaterType.size(); i++) {
                Record record = listWaterType.get(i);
                Integer waterTypeTarget = record.getInt("waters_type");
                Company companyHejiWaterType = new Company();
                companyHejiWaterType.put("streetName", "合计");
                if (record.get("waters_type") != null) {
                    companyHejiWaterType.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(record.get("waters_type")))));
                }
                for (String colKey : meterAttrType.keySet()) {
                    companyHejiWaterType.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record recordC : recordsHejiWaterType) {
                    Integer waters_type = recordC.getInt("waters_type");
                    if (waterTypeTarget == waters_type.intValue()) {
                        String colStr = recordC.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (recordC.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = recordC.getBigDecimal("TargetAttrTotal");
                        }
                        companyHejiWaterType.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                companyHejiWaterType.put("watersUseTotal", watersUseTotal);
                listFinal.add(companyHejiWaterType);//TODO 2
            }

            String sqlXiaoji = "select lsall.street,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr,tad.meter_address from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " lsall.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                    " and lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.street<>'' and lsall.street is not null " +
                    " and lsall.inner_code<>'' and lsall.inner_code is not null and lsall.meter_address<>'' and lsall.meter_address is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.street,lsall.meter_attr order by lsall.street asc";

            List<Record> recordsXiaoji = Db.find(sqlXiaoji);

            List<Record> listStreet = Db.find("select street from t_company where street is not null group by street ");

            for (int i = 0; i < listStreet.size(); i++) {
                Record streetXiaoji = listStreet.get(i);
                Integer streetTarget = streetXiaoji.getInt("street");
                Company companyXiaoji = new Company();
                if (streetTarget != null) {
                    companyXiaoji.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(streetTarget))));
                }
                companyXiaoji.setStreet(streetTarget);
                companyXiaoji.put("watersTypeName", "小计");
                for (String colKey : meterAttrType.keySet()) {
                    companyXiaoji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record record : recordsXiaoji) {
                    Integer street_t = record.getInt("street");
                    if (streetTarget == street_t.intValue()) {
                        String colStr = record.getInt("meter_attr").toString();
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetAttrTotal") != null) {
                            colVal = record.getBigDecimal("TargetAttrTotal");
                        }
                        companyXiaoji.put(ReportColType.street_col + colStr, colVal);
                        watersUseTotal = watersUseTotal.add(colVal);
                    }
                }
                companyXiaoji.put("watersUseTotal", watersUseTotal);
                listFinal.add(companyXiaoji);
                for (Company company : list) {// TODO 3
                    if (streetTarget == company.getStreet().intValue()) {
                        listFinal.add(company);
                    }
                }
            }
        }

        Set<String> columnsTitle = new LinkedHashSet<>();
        Set<String> columnsKey = new LinkedHashSet<>();
        for (String value : meterAttrType.keySet()) {
            columnsTitle.add(meterAttrType.get(value).toString());
            columnsKey.add(ReportColType.street_col + value);
        }
        ExportByDataTypeService service = new ExportByDataTypeService();
        String path = service.exportStreetStatis(listFinal, type, columnsTitle, new ArrayList<>(columnsKey), ReportTypeEnum.STREET);
        renderFile(new File(path));
    }
}
