package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;

/**
 * @Author ssc
 * @Date 2017/8/20 19:12
 * @Version 1.0.0
 * @Desc
 */
public class ReadnumStatisController extends BaseController {

    @RequiresPermissions(value={"/statis/readnum"})
    public void index() {
        render("read_num.jsp");
    }

    @RequiresPermissions(value={"/statis/readnum"})
    public void getListData() {
        String keyword = this.getPara("name");
        Page<?> pageInfo = new Page<Object>();
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }
}
