package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Ad;

/**
 * @Author ssc
 * @Date 2018/9/29 11:42
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class AdController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void find() {
        Ad ad = Ad.dao.findFirst("select * from t_ad where status=1 order by id desc limit 1 ");
        if (ad != null) {
            this.renderJson(ad);
        } else {
            this.renderJson(InvokeResult.failure("暂未发布公告"));
        }
    }
}
