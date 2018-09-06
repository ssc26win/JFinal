package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ReadNumExportService;
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
 * @Date 2017/8/20 19:12
 * @Version 1.0.0
 * @Desc
 */
public class ReadnumStatisController extends BaseController {

    @RequiresPermissions(value={"/statis/readnum"})
    public void index() {
        render("read_num.jsp");
    }

    @RequiresPermissions(value={"/statis/readnum"})
    public void getListData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        String meterAttr = this.getPara("meterAttr");
        String meterAddress = this.getPara("meterAddress");
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
        Date startTime = null;
        Date endTime = null;
        try {
            if (StringUtils.isNotEmpty(this.getPara("startTime"))) {
                startTime = DateUtils.getDate(this.getPara("startTime"), ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.getDate(this.getPara("endTime"), ToolDateTime.pattern_ymd_hms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getReadnumStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("meter_attr")!= null && StringUtils.isNotEmpty(co.get("meter_attr").toString())) {
                    co.put("meterAttrName", String.valueOf(meterAttrType.get(String.valueOf(co.get("meter_attr")))));
                }
                if (co.get("waters_type") != null && StringUtils.isNotEmpty(co.get("waters_type").toString())) {
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

    @RequiresPermissions(value={"/statis/readnum"})
    public void exportData() {
        ActualData.me.setGlobalInnerCode(getInnerCode());
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Integer street = null;
        if (StringUtils.isNotEmpty(this.getPara("street"))) {
            String streetStr = StringUtils.trim(this.getPara("street"));
            street = Integer.parseInt(streetStr);
        }
        String meterAttr = this.getPara("meterAttr");
        String meterAddress = this.getPara("meterAddress");
        Integer watersType = null;
        if (StringUtils.isNotEmpty(this.getPara("watersType"))) {
            String watersTypeStr = StringUtils.trim(this.getPara("watersType"));
            watersType = Integer.parseInt(watersTypeStr);
        }
        Date startTime = null;
        Date endTime = null;
        try {
            if (StringUtils.isNotEmpty(this.getPara("startTime"))) {
                startTime = DateUtils.getDate(this.getPara("startTime"), ToolDateTime.pattern_ymd_hms);
            }
            if (StringUtils.isNotEmpty(this.getPara("endTime"))) {
                endTime = DateUtils.getDate(this.getPara("endTime"), ToolDateTime.pattern_ymd_hms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String type = this.getPara("type");
        Page<ActualData> pageInfo = ActualData.me.getReadnumStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("meter_attr")!= null && StringUtils.isNotEmpty(co.get("meter_attr").toString())) {
                    co.put("meterAttrName", String.valueOf(meterAttrType.get(String.valueOf(co.get("meter_attr")))));
                }
                if (co.get("waters_type") != null && StringUtils.isNotEmpty(co.get("waters_type").toString())) {
                    co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.get("waters_type")))));
                }
                list.set(i, co);
            }
        }
        ReadNumExportService service = new ReadNumExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }
}
