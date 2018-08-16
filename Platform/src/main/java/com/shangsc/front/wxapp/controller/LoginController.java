package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.auth.interceptor.SysLogInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.MyDigestUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.SysLoginRecord;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LoginController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void doLogin() {
        
        renderJson(JsonUtil.obj2Json(new CommonDes()));
    }
}
