package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.DailyExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
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
public class DailyStatisController extends BaseController {

    @RequiresPermissions(value = {"/statis/daily"})
    public void index() {

        String time = this.getPara("time");
        if (StringUtils.isNotBlank(time)) {
            this.setAttr("startTime", time + " 00:00:00");
            this.setAttr("endTime", time + " 23:59:59");
        }

        render("daily_use.jsp");
    }

    @RequiresPermissions(value={"/statis/daily"})
    public void getListData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String street = this.getPara("street");
        Integer watersType = this.getParaToInt("watersType", 0);
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = this.getParaToDate("startTime");
            endTime = this.getParaToDate("endTime");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<ActualData> pageInfo = ActualData.me.getDailyStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("addressMap", "<a href='#' title='点击查看导航地图' style='cursor: pointer;text-decoration: none;'" +
                        " onclick=\"openMap('" + co.get("name").toString() + "', '"
                        + co.get("address").toString() + "', '" + co.getNetWater() + "'" + ")\">" + co.get("address").toString() + "</a>");
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value={"/statis/daily"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String street = this.getPara("street");
        Integer watersType = this.getParaToInt("watersType", 0);
        Date startTime = null;
        Date endTime = null;
        try {
            this.getParaToDate("startTime");
            this.getParaToDate("endTime");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<ActualData> pageInfo = ActualData.me.getDailyStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode, street, watersType);
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                list.set(i, co);
            }
        }
        DailyExportService service = new DailyExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }
}
