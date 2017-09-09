package com.shangsc.platform.controller.basic;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2017/9/10 2:15
 * @Version 1.0.0
 * @Desc
 */
public class SupplyUnitController extends BaseController {

    @RequiresPermissions(value = {"/basic/supply"})
    public void index() {
        render("supply_unit_index.jsp");
    }
}
