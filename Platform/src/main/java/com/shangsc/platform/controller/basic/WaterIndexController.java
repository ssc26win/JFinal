package com.shangsc.platform.controller.basic;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2017/8/20 16:39
 * @Version 1.0.0
 * @Desc
 */
public class WaterIndexController extends BaseController {

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void index() {
        render("water_index.jsp");
    }

}
