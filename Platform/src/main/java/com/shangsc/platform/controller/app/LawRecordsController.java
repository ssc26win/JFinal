package com.shangsc.platform.controller.app;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2018/8/16 11:37
 * @Desc 用途：
 */
public class LawRecordsController extends BaseController {

    @RequiresPermissions(value = {"/basic/law"})
    public void index() {
        render("law_.jsp");
    }
}
