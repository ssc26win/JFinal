package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.MonthExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/21 23:10
 * @Version 1.0.0
 * @Desc
 */
public class MonthStatisController extends BaseController {

    @RequiresPermissions(value = {"/statis/month"})
    public void index() {
        String time = this.getPara("time");
        if (StringUtils.isNotBlank(time)) {
            this.setAttr("startTime", time);
            this.setAttr("endTime", time);
        }
        String type = this.getPara("type");
        if (StringUtils.isNotEmpty(type)) {
            this.setAttr("type", type);
        }
        render("month_use.jsp");
    }

    @RequiresPermissions(value={"/statis/month"})
    public void getListData() {
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
                startTime = DateUtils.parseDate(this.getPara("startTime") + "-01 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.parseDate(this.getPara("endTime") + "-01 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (endTime!=null) {
                endTime = DateUtils.addMonth(endTime, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getMonthStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                String monthTotal = "0";
                if (co.get("monthTotal") != null) {
                    monthTotal = co.get("monthTotal").toString();
                }
                co.put("monthTotal", monthTotal);
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

    @RequiresPermissions(value={"/statis/month"})
    public void exportData() {
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
                startTime = DateUtils.parseDate(this.getPara("startTime") + "-01 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.parseDate(this.getPara("endTime") + "-01 00:00:00", ToolDateTime.pattern_ymd_hms);
            }
            if (endTime!=null) {
                endTime = DateUtils.addMonth(endTime, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getMonthStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                String monthTotal = "0";
                if (co.get("monthTotal") != null) {
                    monthTotal = co.get("monthTotal").toString();
                }
                co.put("monthTotal", monthTotal);
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
        MonthExportService service = new MonthExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}
