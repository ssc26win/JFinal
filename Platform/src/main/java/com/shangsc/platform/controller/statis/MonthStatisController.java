package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.Company;

import java.io.File;
import java.util.Date;

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

        Page<Company> pageInfo = Company.me.getMonthStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);

        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
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
