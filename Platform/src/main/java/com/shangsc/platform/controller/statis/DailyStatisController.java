package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.DataStatis;

import java.io.File;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/21 23:06
 * @Version 1.0.0
 * @Desc
 */
public class DailyStatisController extends BaseController {

    private DataStatis dataStatis = new DataStatis();

    @RequiresPermissions(value = {"/statis/daily"})
    public void index() {
        render("daily_use.jsp");
    }

    @RequiresPermissions(value={"/statis/daily"})
    public void getListData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        Page<?> pageInfo = dataStatis.getDailyStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);

        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }


    @RequiresPermissions(value={"/statis/daily"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        dataStatis.exportDailyData(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);
        File file = new File("");
        this.renderFile(file);
    }
}
