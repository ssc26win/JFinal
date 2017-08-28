package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.YearExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;

import java.io.File;
import java.util.Date;
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
        Integer year = this.getParaToInt("year");
        Date startTime = null;
        Date endTime = null;
        try {
            this.getParaToDate("startTime");
            this.getParaToDate("endTime");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<ActualData> pageInfo = ActualData.me.getYearStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("addressMap", "<a href='#' title='点击查看导航地图' style='cursor: pointer;text-decoration: none;'" +
                        " onclick=\"openMap('" + co.get("companyName").toString() + "', '"
                        + co.get("address").toString() + "', '0'" + ")\">" + co.get("address").toString() + "</a>");
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value={"/statis/year"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = null;
        Date endTime = null;
        try {
            this.getParaToDate("startTime");
            this.getParaToDate("endTime");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<ActualData> pageInfo = ActualData.me.getYearStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                list.set(i, co);
            }
        }
        YearExportService service = new YearExportService();
        //TODO
        String path = service.export(list, DateUtils.getThisMonth());
        renderFile(new File(path));
    }
}
