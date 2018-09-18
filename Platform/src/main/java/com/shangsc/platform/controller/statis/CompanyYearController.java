package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.CompanyYearExportService;
import com.shangsc.platform.export.YearExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
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
public class CompanyYearController extends BaseController {

    @RequiresPermissions(value = {"/statis/cpayear"})
    public void index() {
        render("company_year_use.jsp");
    }

    @RequiresPermissions(value = {"/statis/cpayear"})
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
        Page<ActualData> pageInfo = ActualData.me.getCPAYearStatis(getPage(), getRows(), getOrderbyStr(),
                year, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                String yearTotal = "0";
                if (co.get("yearTotal") != null) {
                    yearTotal = co.get("yearTotal").toString();
                }
                co.put("yearTotal", yearTotal);
                if (co.get("address") != null) {
                    co.put("addressMap", "<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('"
                            + co.get("inner_code") + "')\">" + co.get("address").toString() + "</a>");
                }
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/statis/cpayear"})
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
        Page<ActualData> pageInfo = ActualData.me.getCPAYearStatis(getPage(), GlobalConfig.EXPORT_SUM, getOrderbyStr(),
                year, name, innerCode, street, watersType, meterAttr, meterAddress, type);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
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
        CompanyYearExportService service = new CompanyYearExportService();
        String path = service.export(list, type);
        renderFile(new File(path));
    }
}
