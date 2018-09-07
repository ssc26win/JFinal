package com.shangsc.platform.controller.report;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ExportByDataTypeService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataReport;
import com.shangsc.platform.model.Company;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/21 23:11
 * @Version 1.0.0
 * @Desc
 */
public class ReportYearController extends BaseController {

    @RequiresPermissions(value = {"/report/year"})
    public void index() {
        JSONArray array = new JSONArray();
        Map<String, String> years = ActualDataReport.me.getYearColumns();
        JSONObject company = new JSONObject();
        company.put("label", "单位名称");
        company.put("name", "companyName");
        company.put("width", "100px;");
        company.put("sortable", "false");
        array.add(company);
        for (String value : years.keySet()) {
            JSONObject column = new JSONObject();
            column.put("label", value);
            column.put("name", value);
            column.put("width", "100px;");
            column.put("sortable", "false");
            array.add(column);
        }
        this.setAttr("columnsYear", array);
        Map<String, String> nameList = Company.me.loadNameList();
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));
        render("year_report.jsp");
    }

    @RequiresPermissions(value = {"/report/year"})
    public void getListData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String type = this.getPara("type");
        Page<Company> pageInfo = ActualDataReport.me.getCompanies(getPage(), getRows(), getOrderbyStr(), name, innerCode, type);
        List<Company> list = pageInfo.getList();
        Map<String, String> yearColumns = ActualDataReport.me.getYearColumns();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> innerCodes = new HashSet<>();
            for (Company company : list) {
                innerCodes.add("'" + company.getInnerCode() + "'");
            }
            String sql = "select tad.inner_code,date_format(tad.write_time, '%Y') as TargetDT, sum(tad.net_water) as TargetTotal " +
                    " from t_actual_data tad where tad.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    "group by tad.inner_code,date_format(tad.write_time, '%Y') order by TargetDT asc";
            List<Record> records = Db.find(sql);
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                for (String colKey : yearColumns.keySet()) {
                    company.put(colKey, new BigDecimal("0"));
                }
                String innerCodeTarget = company.getInnerCode();
                for (Record record : records) {
                    String inner_code = record.getStr("inner_code");
                    if (innerCodeTarget.equals(inner_code)) {
                        String colStr = record.getStr("TargetDT");
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetTotal") != null) {
                            colVal = record.getBigDecimal("TargetTotal");
                        }
                        company.put(colStr, colVal);
                    }
                }
                list.set(i, company);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/report/year"})
    public void exportData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String type = this.getPara("type");
        Page<Company> pageInfo = ActualDataReport.me.getCompanies(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(), name, innerCode, type);
        List<Company> list = pageInfo.getList();
        Map<String, String> yearColumns = ActualDataReport.me.getYearColumns();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> innerCodes = new HashSet<>();
            for (Company company : list) {
                innerCodes.add("'" + company.getInnerCode() + "'");
            }
            String sql = "select tad.inner_code,date_format(tad.write_time, '%Y') as TargetDT, sum(tad.net_water) as TargetTotal " +
                    " from t_actual_data tad where tad.inner_code in (" + StringUtils.join(innerCodes, ",") + ")" +
                    "group by tad.inner_code,date_format(tad.write_time, '%Y') order by tad.inner_code asc,TargetDT asc";

            List<Record> records = Db.find(sql);
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                for (String colKey : yearColumns.keySet()) {
                    company.put(colKey, new BigDecimal("0"));
                }
                String innerCodeTarget = company.getInnerCode();
                for (Record record : records) {
                    String inner_code = record.getStr("inner_code");
                    if (innerCodeTarget.equals(inner_code)) {
                        String colStr = record.getStr("TargetDT");
                        BigDecimal colVal = new BigDecimal("0");
                        if (record.getBigDecimal("TargetTotal") != null) {
                            colVal = record.getBigDecimal("TargetTotal");
                        }
                        company.put(colStr, colVal);
                    }
                }
                list.set(i, company);
            }
        }
        ExportByDataTypeService service = new ExportByDataTypeService();
        String path = service.exportCompanyStatis(list, type, yearColumns, ReportTypeEnum.YEAR);
        renderFile(new File(path));
    }
}
