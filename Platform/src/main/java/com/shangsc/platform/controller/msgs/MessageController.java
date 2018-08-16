package com.shangsc.platform.controller.msgs;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2018/8/16 10:42
 * @Desc 用途：
 */

public class MessageController extends BaseController {

    @RequiresPermissions(value = {"/basic/msg"})
    public void index() {
        render("msg_index.jsp");
    }
}
