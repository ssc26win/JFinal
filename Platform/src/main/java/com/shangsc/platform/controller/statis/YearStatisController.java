package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.YearNum;

import java.io.File;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/21 23:11
 * @Version 1.0.0
 * @Desc
 */
public class YearStatisController extends BaseController {

    private YearNum dataStatis = new YearNum();

    @RequiresPermissions(value = {"/statis/year"})
    public void index() {
        render("year_use.jsp");
    }

    @RequiresPermissions(value={"/statis/year"})
    public void getListData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        Page<?> pageInfo = dataStatis.getYearStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);

        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/statis/year"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        dataStatis.exportYearData(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);
        File file = new File("");
        this.renderFile(file);
    }
}
