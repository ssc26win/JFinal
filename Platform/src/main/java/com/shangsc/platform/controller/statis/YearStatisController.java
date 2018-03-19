package com.shangsc.platform.controller.statis;

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
public class YearStatisController extends BaseController {

    @RequiresPermissions(value = {"/statis/year"})
    public void index() {
        render("year_use.jsp");
    }

    @RequiresPermissions(value={"/statis/year"})
    public void getListData() {
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

    @RequiresPermissions(value={"/statis/year"})
    public void exportData() {
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
