package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;

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
        String keyword = this.getPara("name");
        //Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        Page<?> pageInfo = new Page<Object>();
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }
}
