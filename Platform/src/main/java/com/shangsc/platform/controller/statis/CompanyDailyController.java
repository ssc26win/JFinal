package com.shangsc.platform.controller.statis;

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
import com.shangsc.platform.export.CompanyDailyExportService;
import com.shangsc.platform.export.DailyExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/21 23:06
 * @Version 1.0.0
 * @Desc
 */
public class CompanyDailyController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/statis/cpadaily"})
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
        render("company_daily_use.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/statis/cpadaily"})
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
        Page<ActualData> pageInfo = ActualData.me.getCPADailyStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("address") != null) {
                    co.put("addressMap", "<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('"
                            + co.get("inner_code") + "')\">" + co.get("address").toString() + "</a>");
                }
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/statis/cpadaily"})
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
        Page<ActualData> pageInfo = ActualData.me.getCPADailyStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        CompanyDailyExportService service = new CompanyDailyExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}