package com.shangsc.platform.controller.report;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.YearExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/21 23:11
 * @Version 1.0.0
 * @Desc
 */
public class ReportStreetController extends BaseController {

    /**
     * [
     { label: '所属乡镇', name: 'water_unit', width: 100, sortable:false},
     { label: '单位名称', name: 'name', width: 200, sortable:false},
     { label: '单位编号', name: 'inner_code', width: 80, sortable:false},
     { label: '路别', name: 'line_num', width: 60, sortable:false},
     { label: '水表编号', name: 'meter_num', width: 60,sortable:false},
     { label: '表计地址', name: 'meter_address', width: 100,sortable:false},
     { label: '水表属性', name: 'meter_attr', width: 100, sortable:false},
     { label: '查询时间', name: 'years', width: 60, sortable:true},
     { label: '用水量（立方米）', name: 'yearTotal', width: 100, sortable:true},
     { label: '单位地址', name: 'addressMap', width: 150,sortable:false}
     ]
     */
    @RequiresPermissions(value = {"/report/street"})
    public void index() {

        render("street_report.jsp");
    }

    @RequiresPermissions(value={"/report/street"})
    public void getListData() {
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
        Page<ActualData> pageInfo = ActualData.me.getYearStatis(getPage(), getRows(), getOrderbyStr(),
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
                    co.put("watersTypeName","");
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

    @RequiresPermissions(value={"/report/street"})
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
                    co.put("watersTypeName","");
                }
                list.set(i, co);
            }
        }
        YearExportService service = new YearExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}
