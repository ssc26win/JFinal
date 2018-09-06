package com.shangsc.platform.controller.report;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.YearExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataResport;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
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
        List<String> years = ActualDataResport.me.getYearColumns();
        JSONObject company = new JSONObject();
        company.put("label", "单位名称");
        company.put("name", "companyName");
        company.put("width", "100px;");
        company.put("sortable", "false");
        array.add(company);
        for (String value : years) {
            JSONObject column = new JSONObject();
            column.put("label", value);
            column.put("name", "Year_" + value);
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
        Page<ActualData> pageInfo = ActualDataResport.me.getYear(getPage(), getRows(), getOrderbyStr(), name, innerCode, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                String yearTotal = "0";
                if (co.get("yearTotal") != null) {
                    yearTotal = co.get("yearTotal").toString();
                }
                co.put("yearTotal", yearTotal);
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/report/year"})
    public void exportData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Integer year = null;
        if (StringUtils.isNotEmpty(this.getPara("year"))) {
            String yearStr = StringUtils.trim(this.getPara("year"));
            year = Integer.parseInt(yearStr);
        }
        String meterAttr = this.getPara("meterAttr");
        String meterAddress = this.getPara("meterAddress");
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
        Page<ActualData> pageInfo = ActualData.me.getYearStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                year, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                String yearTotal = "0";
                if (co.get("yearTotal") != null) {
                    yearTotal = co.get("yearTotal").toString();
                }
                co.put("yearTotal", yearTotal);
                if (co.get("waters_type") != null) {
                    String watersTypeStr = co.get("waters_type").toString();
                    if (mapWatersType.get(watersTypeStr) != null) {
                        co.put("watersTypeName", String.valueOf(mapWatersType.get(watersTypeStr)));
                    }
                } else {
                    co.put("watersTypeName", "");
                }
                list.set(i, co);
            }
        }
        YearExportService service = new YearExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}
