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
public class ReportCompanyController extends BaseController {

    @RequiresPermissions(value = {"/report/company"})
    public void index() {
        JSONArray array = new JSONArray();
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONObject unit = new JSONObject();
        unit.put("label", "所属节水办");
        unit.put("name", "water_unit");
        unit.put("width", "200");
        unit.put("sortable", "false");
        array.add(unit);
        JSONObject companyI = new JSONObject();
        companyI.put("label", "用户编号");
        companyI.put("name", "real_code");
        companyI.put("width", "80");
        companyI.put("sortable", "false");
        array.add(companyI);
        JSONObject company = new JSONObject();
        company.put("label", "用户名称");
        company.put("name", "name");
        company.put("width", "300");
        company.put("sortable", "false");
        array.add(company);
        JSONObject waterType = new JSONObject();
        waterType.put("label", "水源类型");
        waterType.put("name", "watersTypeName");
        waterType.put("width", "100");
        waterType.put("sortable", "false");
        array.add(waterType);
        JSONObject waterUseTotal = new JSONObject();
        waterUseTotal.put("label", "总用水量");
        waterUseTotal.put("name", "watersUseTotal");
        waterUseTotal.put("width", "100");
        waterUseTotal.put("sortable", "false");
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
            column.put("sortable", "false");
            array.add(column);
        }
        this.setAttr("columnsMeterAttr", array);
        this.setAttr("watersType", this.getPara("watersType"));
        render("company_report.jsp");
    }

    @RequiresPermissions(value = {"/report/company"})
    public void getListData() {
        ActualDataReport.me.setGlobalInnerCode(getInnerCodesSQLStr());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
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

        Page<Company> pageInfo = ActualDataReport.me.getCompany(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), street, name, innerCode, watersType, type);
        List<Company> list = pageInfo.getList();
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);

        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> innerCodes = new HashSet<>();
            Set<Integer> watersTypes = new HashSet<>();
            for (Company company : list) {
                innerCodes.add("'" + company.getInnerCode() + "'");
                watersTypes.add(company.getInt("waters_type"));
            }

            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

            String sql = "select lsall.name,lsall.inner_code,lsall.street,lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.name,tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null" +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.inner_code,lsall.waters_type,lsall.meter_attr order by lsall.street asc,lsall.waters_type asc";

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
                String innerCodeTarget = company.getInnerCode();
                for (Record record : records) {
                    Integer waters_type = record.getInt("waters_type");
                    String innerCode_t = record.getStr("inner_code");
                    if (waterTypeTarget == waters_type.intValue() && innerCodeTarget.equals(innerCode_t)) {
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
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.meter_attr order by lsall.waters_type asc";
            List<Record> recordsZongji = Db.find(sqlZongji);
            Company companyZongji = new Company();
            companyZongji.put("water_unit", "通州区节水办");
            companyZongji.put("name", "合计");
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
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.waters_type,lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsHejiWaterType = Db.find(sqlHejiWaterType);

            List<Record> listWaterType = Db.find("select waters_type from t_water_meter where waters_type is not null " +
                    " and waters_type in (" + StringUtils.join(watersTypes, ",") + ") " +
                    " group by waters_type");

            for (int i = 0; i < listWaterType.size(); i++) {
                Record record = listWaterType.get(i);
                Integer waterTypeTarget = record.getInt("waters_type");
                Company companyHejiWaterType = new Company();
                companyHejiWaterType.put("water_unit", "通州区节水办");
                companyHejiWaterType.put("name", "合计");
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

            String sqlXiaoji = "select lsall.water_unit,lsall.name,lsall.real_code,lsall.inner_code,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.water_unit,tc.name,tc.real_code,tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.inner_code,lsall.meter_attr order by lsall.inner_code asc";

            List<Record> recordsXiaoji = Db.find(sqlXiaoji);

            List<Record> listStreet = Db.find("select water_unit,name,real_code,inner_code from t_company where inner_code is not null" +
                    " and inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " group by inner_code ");

            for (int i = 0; i < listStreet.size(); i++) {
                Record streetXiaoji = listStreet.get(i);
                String innerCodeTarget = streetXiaoji.getStr("inner_code");
                Company companyXiaoji = new Company();
                companyXiaoji.put("water_unit", streetXiaoji.getStr("water_unit"));
                companyXiaoji.put("real_code", streetXiaoji.getStr("real_code"));
                companyXiaoji.put("name", streetXiaoji.getStr("name"));
                companyXiaoji.put("watersTypeName", "小计");
                for (String colKey : meterAttrType.keySet()) {
                    companyXiaoji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record record : recordsXiaoji) {
                    String innerCode_t = record.getStr("inner_code");
                    if (innerCodeTarget.equals(innerCode_t)) {
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
                    if (innerCodeTarget.equals(company.getInnerCode())) {
                        listFinal.add(company);
                    }
                }
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, listFinal));
    }

    @RequiresPermissions(value = {"/report/company"})
    public void exportData() {
        ActualDataReport.me.setGlobalInnerCode(getInnerCodesSQLStr());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
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

        Page<Company> pageInfo = ActualDataReport.me.getCompany(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), street, name, innerCode, watersType, type);
        List<Company> list = pageInfo.getList();
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);

        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> innerCodes = new HashSet<>();
            Set<Integer> watersTypes = new HashSet<>();
            for (Company company : list) {
                innerCodes.add("'" + company.getInnerCode() + "'");
                watersTypes.add(company.getInt("waters_type"));
            }

            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

            String sql = "select lsall.name,lsall.inner_code,lsall.street,lsall.waters_type,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.name,tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null" +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.inner_code,lsall.waters_type,lsall.meter_attr order by lsall.street asc,lsall.waters_type asc";

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
                String innerCodeTarget = company.getInnerCode();
                for (Record record : records) {
                    Integer waters_type = record.getInt("waters_type");
                    String innerCode_t = record.getStr("inner_code");
                    if (waterTypeTarget == waters_type.intValue() && innerCodeTarget.equals(innerCode_t)) {
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
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.meter_attr order by lsall.waters_type asc";
            List<Record> recordsZongji = Db.find(sqlZongji);
            Company companyZongji = new Company();
            companyZongji.put("water_unit", "通州区节水办");
            companyZongji.put("name", "合计");
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
                    "(select tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.waters_type,lsall.meter_attr order by lsall.waters_type asc";

            List<Record> recordsHejiWaterType = Db.find(sqlHejiWaterType);

            List<Record> listWaterType = Db.find("select waters_type from t_water_meter where waters_type is not null " +
                    " and waters_type in (" + StringUtils.join(watersTypes, ",") + ") " +
                    " group by waters_type");

            for (int i = 0; i < listWaterType.size(); i++) {
                Record record = listWaterType.get(i);
                Integer waterTypeTarget = record.getInt("waters_type");
                Company companyHejiWaterType = new Company();
                companyHejiWaterType.put("water_unit", "通州区节水办");
                companyHejiWaterType.put("name", "合计");
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

            String sqlXiaoji = "select lsall.water_unit,lsall.name,lsall.real_code,lsall.inner_code,lsall.meter_attr,COALESCE(sum(lsall.net_water), 0) as TargetAttrTotal from " +
                    "(select tc.water_unit,tc.name,tc.real_code,tc.street,tad.net_water,tad.inner_code,tad.write_time,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    (watersType != null ? " and lsall.waters_type=" + watersType : "") +
                    (meterAttr != null ? " and lsall.meter_attr=" + meterAttr : "") +
                    (startTime != null ? " and lsall.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    (endTime != null ? " and lsall.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : "") +
                    " group by lsall.inner_code,lsall.meter_attr order by lsall.inner_code asc";

            List<Record> recordsXiaoji = Db.find(sqlXiaoji);

            List<Record> listStreet = Db.find("select water_unit,name,real_code,inner_code from t_company where inner_code is not null " +
                    " and inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    " group by inner_code ");

            for (int i = 0; i < listStreet.size(); i++) {
                Record streetXiaoji = listStreet.get(i);
                String innerCodeTarget = streetXiaoji.getStr("inner_code");
                Company companyXiaoji = new Company();
                companyXiaoji.put("water_unit", streetXiaoji.getStr("water_unit"));
                companyXiaoji.put("real_code", streetXiaoji.getStr("real_code"));
                companyXiaoji.put("name", streetXiaoji.getStr("name"));
                companyXiaoji.put("watersTypeName", "小计");
                for (String colKey : meterAttrType.keySet()) {
                    companyXiaoji.put(ReportColType.street_col + colKey, new BigDecimal("0"));
                }
                BigDecimal watersUseTotal = new BigDecimal("0");
                for (Record record : recordsXiaoji) {
                    String innerCode_t = record.getStr("inner_code");
                    if (innerCodeTarget.equals(innerCode_t)) {
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
                    if (innerCodeTarget.equals(company.getInnerCode())) {
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
        String path = service.exportCompanyStatis(listFinal, type, columnsTitle, new ArrayList<>(columnsKey), ReportTypeEnum.STREET);
        renderFile(new File(path));
    }
}
