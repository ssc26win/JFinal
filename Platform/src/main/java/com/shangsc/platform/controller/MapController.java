package com.shangsc.platform.controller;

import com.jfinal.core.Controller;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;

/**
 * @Author ssc
 * @Date 2017/8/23 11:54
 * @Desc 用途：
 */
public class MapController extends Controller {

    @RequiresPermissions(value={"/map"})
    public void index() {
        render("map.jsp");
    }
}
