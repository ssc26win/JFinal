package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;
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
@Clear(SysLogInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LoginController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void doLogin() {
        String imageCode = this.getPara("imageCode");
        if (StrKit.notBlank(imageCode)) {
            String imageCodeSession = (String) this.getSessionAttr("imageCode");
            if (!imageCodeSession.toLowerCase().equals(imageCode.trim().toLowerCase())) {
                this.renderJson(InvokeResult.failure("验证码输入有误"));
                return;
            }
        } else {
            this.renderJson(InvokeResult.failure("请输入验证码"));
            return;
        }
        SysUser sysUser = SysUser.me.getByName(this.getPara("username"));
        if (sysUser == null) {
            sysUser = SysUser.me.getByEmail(this.getPara("username"));
            if (sysUser == null) {
                sysUser = SysUser.me.getByPhone(this.getPara("username"));
            }
        }
        if (sysUser == null) {
            this.renderJson(InvokeResult.failure("用户不存在"));
            return;
        }
        if (SysLoginRecord.dao.hasOverLoginErrTimes(sysUser.getId())) {
            this.renderJson(InvokeResult.failure("今天连续输入密码错误次数超过5次"));
            return;
        }
        if (!sysUser.getPwd().equals(MyDigestUtils.shaDigestForPasswrod(this.getPara("password")))) {
            SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(), 0);
            this.renderJson(InvokeResult.failure("用户密码输入有误"));
            return;
        }
        if (sysUser.getInt("status") == 2) {
            this.renderJson(InvokeResult.failure("用户被冻结，请联系管理员"));
            return;
        }
        Integer autoLogin = this.getParaToInt("autoLogin", 0);
        IWebUtils.setCurrentLoginSysUser(this.getResponse(), this.getSession(), sysUser, autoLogin);
        SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(), 1);
        this.renderJson(InvokeResult.success());
    }
}
