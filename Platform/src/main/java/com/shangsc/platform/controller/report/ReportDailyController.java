package com.shangsc.platform.controller.report;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.DailyExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataResport;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/21 23:06
 * @Version 1.0.0
 * @Desc
 */
public class ReportDailyController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/report/daily"})
    public void index() {
        JSONArray array = new JSONArray();
        List<String> months = ActualDataResport.me.getDayColumns();
        JSONObject company = new JSONObject();
        company.put("label", "单位名称");
        company.put("name", "companyName");
        company.put("width", "100px;");
        company.put("sortable", "false");
        array.add(company);
        for (String value : months) {
            JSONObject column = new JSONObject();
            column.put("label", value);
            column.put("name", "Day_" + value);
            column.put("width", "100px;");
            column.put("sortable", "false");
            array.add(column);
        }
        this.setAttr("columnsDay", array);
        String type = this.getPara("type");
        if (StringUtils.isNotEmpty(type)) {
            this.setAttr("type", type);
        }
        Map<String, String> nameList = Company.me.loadNameList();
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));
        render("daily_report.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/report/daily"})
    public void getListData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String meterAddress = this.getPara("meterAddress");
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        String meterAttr = this.getPara("meterAttr");
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
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
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getDailyStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("waters_type") != null) {
                    String watersTypeStr = co.get("waters_type").toString();
                    if (mapWatersType.get(watersTypeStr) != null) {
                        co.put("watersTypeName", String.valueOf(mapWatersType.get(watersTypeStr)));
                    }
                } else {
                    co.put("watersTypeName", "");
                }
                if (co.get("address") != null) {
                    co.put("addressMap", "<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('"
                            + co.get("inner_code") + "')\">" + co.get("address").toString() + "</a>");
                }
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/report/daily"})
    public void exportData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String meterAddress = this.getPara("meterAddress");
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        String meterAttr = this.getPara("meterAttr");
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
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
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getDailyStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
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
        DailyExportService service = new DailyExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}
