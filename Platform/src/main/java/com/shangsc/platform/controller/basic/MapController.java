package com.shangsc.platform.controller.basic;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2017/8/24 0:26
 * @Version 1.0.0
 * @Desc
 */
public class MapController  extends BaseController {

    @RequiresPermissions(value={"/map"})
    public void index() {
        render("map.jsp");
    }
}
