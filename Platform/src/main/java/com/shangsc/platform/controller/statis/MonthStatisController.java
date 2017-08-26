package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;

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
        render("month_use.jsp");
    }

    @RequiresPermissions(value={"/statis/month"})
    public void getListData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");
        Page<ActualData> pageInfo = ActualData.me.getMonthStatis(getPage(), getRows(), getOrderbyStr(),
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
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value={"/statis/month"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        //dataStatis.exportMonthData(getPage(), getRows(), getOrderbyStr(),
        //        startTime, endTime, name, innerCode);
        File file = new File("");
        this.renderFile(file);
    }
}
