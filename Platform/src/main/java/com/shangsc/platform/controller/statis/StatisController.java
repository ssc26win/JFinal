package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.Company;

/**
 * @Author ssc
 * @Date 2017/8/20 19:12
 * @Version 1.0.0
 * @Desc
 */
public class StatisController extends BaseController {

    @RequiresPermissions(value={"/statis/readnum"})
    public void indexReadnum() {
        render("read_num.jsp");
    }

    @RequiresPermissions(value={"/statis/daily"})
    public void indexDailyuse() {
        render("daily_use.jsp");
    }

    @RequiresPermissions(value={"/statis/month"})
    public void indexMonthuse() {
        render("month_use.jsp");
    }

    @RequiresPermissions(value={"/statis/year"})
    public void indexYearuse() {
        render("year_use.jsp");
    }

    @RequiresPermissions(value={"/statis/readnum"})
    public void getreadnumListData() {
        String keyword = this.getPara("name");
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/statis/daily"})
    public void getdailyListData() {
        String keyword = this.getPara("name");
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/statis/month"})
    public void getmonthListData() {
        String keyword = this.getPara("name");
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/statis/year"})
    public void getyearListData() {
        String keyword = this.getPara("name");
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }
}
