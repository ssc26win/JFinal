package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LoginController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void doLoginWxApp() {

        renderJson(JsonUtil.obj2Json(new CommonDes()));
    }
}
