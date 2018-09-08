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
public class ReportStreetController extends BaseController {

    @RequiresPermissions(value = {"/report/street"})
    public void index() {
        JSONArray array = new JSONArray();
        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
        JSONObject street = new JSONObject();
        street.put("label", "所属乡镇");
        street.put("name", "streetName");
        street.put("width", "120");
        street.put("sortable", "false");
        array.add(street);
        JSONObject company = new JSONObject();
        company.put("label", "水源类型");
        company.put("name", "watersTypeName");
        company.put("width", "100");
        company.put("sortable", "false");
        array.add(company);
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
        render("street_report.jsp");
    }

    @RequiresPermissions(value = {"/report/street"})
    public void getListData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
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
        String type = this.getPara("type");
        Page<Company> pageInfo = ActualDataReport.me.getStreet(getPage(), getRows(), getOrderbyStr(), street, watersType, type);
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

            String sql = "select lsall.street,lsall.waters_type,lsall.meter_attr,sum(lsall.net_water) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    " group by lsall.street,lsall.waters_type,lsall.meter_attr order by lsall.street asc";

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
                    }
                }
                list.set(i, company);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/report/street"})
    public void exportData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
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
        String type = this.getPara("type");
        Page<Company> pageInfo = ActualDataReport.me.getStreet(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), street, watersType, type);
        List<Company> list = pageInfo.getList();

        Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);

        if (CollectionUtils.isNotEmpty(list)) {
            Set<Integer> streets = new HashSet<>();
            Set<Integer> watersTypes = new HashSet<>();
            for (Company company : list) {
                streets.add(company.getStreet());
                watersTypes.add(company.getInt("waters_type"));
            }

            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);

            String sql = "select lsall.street,lsall.waters_type,lsall.meter_attr,sum(lsall.net_water) as TargetAttrTotal from " +
                    "(select tc.street,tad.net_water,tad.inner_code,twm.waters_type,twm.meter_attr from t_actual_data tad " +
                    " left join t_water_meter twm on twm.meter_address=tad.meter_address " +
                    " left join t_company tc on tc.inner_code=tad.inner_code) lsall " +
                    " where lsall.street in (" + StringUtils.join(streets, ",") + ")" +
                    " and lsall.waters_type in (" + StringUtils.join(watersTypes, ",") + ")" +
                    " and lsall.meter_attr<>'' and lsall.meter_attr is not null " +
                    " group by lsall.street,lsall.waters_type,lsall.meter_attr order by lsall.street asc";

            List<Record> records = Db.find(sql);
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getStreet() != null) {
                    company.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(company.getStreet()))));
                }
                if (company.get("waters_type") != null) {
                    company.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(company.get("waters_type")))));
                }
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
                    }
                }
                list.set(i, company);
            }
        }

        Set<String> columnsTitle = new LinkedHashSet<>();
        Set<String> columnsKey = new LinkedHashSet<>();
        for (String value : meterAttrType.keySet()) {
            columnsTitle.add(meterAttrType.get(value).toString());
            columnsKey.add(ReportColType.street_col + value);
        }
        ExportByDataTypeService service = new ExportByDataTypeService();
        String path = service.exportStreetStatis(list, type, columnsTitle, new ArrayList<>(columnsKey), ReportTypeEnum.STREET);
        renderFile(new File(path));
    }
}
