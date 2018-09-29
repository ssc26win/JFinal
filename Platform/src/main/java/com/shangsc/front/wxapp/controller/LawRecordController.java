package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;

/**
 * @Author ssc
 * @Date 2018/8/16 11:09
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LawRecordController extends BaseController {

}
