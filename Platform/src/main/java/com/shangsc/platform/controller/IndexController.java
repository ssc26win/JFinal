/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.controller;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.auth.interceptor.SysLogInterceptor;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.MyDigestUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.mail.MailKit;
import com.shangsc.platform.model.SysLoginRecord;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页、登陆、登出
 * @author ssc
 */
@Clear(SysLogInterceptor.class)
@RequiresPermissions(value = {"/"})
public class IndexController extends Controller {

    public void index() {
        render("index.jsp");
    }

    public void home() {
        render("home.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    public void login() {
        this.setAttr("url", this.getPara("url"));
        if (StringUtils.isNotEmpty(this.getPara("email"))) {
            this.setAttr("email", this.getPara("email"));
        }
        render("login.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    public void dologin() {
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

    public void loginOut() {
        IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
        this.redirect("/login");
    }

    public void pwdSetting() {
        try {
            SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
            this.setAttr("sysUser", sysUser);
            render("sys/pwd_setting.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePwdUpdate() {
        String oldPwd = MyDigestUtils.shaDigestForPasswrod(getPara("oldPwd"));
        String newPwd = MyDigestUtils.shaDigestForPasswrod(getPara("newPwd"));
        String pwdRepeat = MyDigestUtils.shaDigestForPasswrod(getPara("pwdRepeat"));
        try {
            SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
            if (!sysUser.get("pwd").equals(oldPwd)) {
                this.renderJson(InvokeResult.failure(-3, "旧密码不正确"));
            } else {
                if (newPwd.equals(pwdRepeat)) {
                    InvokeResult result = SysUser.me.savePwdUpdate(sysUser.getInt("id"), newPwd);
                    this.renderJson(result);
                } else {
                    this.renderJson(InvokeResult.failure(-1, "两次密码不一致"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void regist() {
        IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
        String username = this.getPara("username");
        String password = this.getPara("password");
        String phone = this.getPara("phone");
        String email = this.getPara("email");
        InvokeResult result = SysUser.me.regist(username, password, phone, email);
        SysUser sysUser = SysUser.me.getByName(username);
        IWebUtils.setCurrentLoginSysUser(this.getResponse(), this.getSession(), sysUser, 0);
        this.renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void resetPwdSendEmail() {
        IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
        String email = this.getPara("email");
        SysUser sysUser = SysUser.me.getByEmail(email);
        if (sysUser == null) {
            this.renderJson(InvokeResult.failure("邮箱不存在"));
            return;
        }
        List<String> to = new ArrayList<String>();            // 邮件接收者的地址
        to.add(email);
        String subject = "通州区远传水表采集系统-找回密码";        // 邮件标题
        StringBuilder content = new StringBuilder("<h2>重置密码</h2>");// 邮件的文本内容
        content.append("<br/>");
        content.append("<a href='");
        content.append(PropKit.get("config.host.url"));
        content.append("/login");
        content.append("?email=" + email);
        content.append("&time=" + System.currentTimeMillis());
        content.append("' target='_blank' ><b>点击</b>重置密码(30分钟内有效)</a>");
        this.setCookie("find_password_by_email", String.valueOf(System.currentTimeMillis()), 1000 * 60 * 30);
        try {
            //MailKit.send(email, null, subject, content.toString());
            MailKit.sendEmail465(subject, content.toString(), email);
        } catch (Exception e) {

        }
        this.renderJson(InvokeResult.success());
    }

    @Clear(AuthorityInterceptor.class)
    public void savePwdForget() {
        String times = this.getCookie("find_password_by_email");
        if (StringUtils.isEmpty(times)) {
            this.renderJson(InvokeResult.failure("超出找回密码邮件时间"));
            return;
        }
        String email = this.getPara("email");
        String newPwd = MyDigestUtils.shaDigestForPasswrod(getPara("password"));
        try {
            SysUser sysUser = SysUser.me.getByEmail(email);
            if (sysUser == null) {
                this.renderJson(InvokeResult.failure("邮箱不存在"));
                return;
            }
            InvokeResult result = SysUser.me.savePwdUpdate(sysUser.getInt("id"), newPwd);
            this.renderJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





