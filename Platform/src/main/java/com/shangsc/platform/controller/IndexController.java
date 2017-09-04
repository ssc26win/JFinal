/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.controller;

import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.shangsc.platform.core.util.MyDigestUtils;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.auth.interceptor.SysLogInterceptor;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.SysLoginRecord;
import com.shangsc.platform.model.SysUser;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.shangsc.platform.util.ToolMail;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页、登陆、登出
 * @author ssc
 */
@Clear(SysLogInterceptor.class)
@RequiresPermissions(value={"/"})
public class IndexController extends Controller {

	private static final Log log = Log.getLog(IndexController.class);
	
	public void index() {
		render("index.jsp");
	}
	
	public void home() {
		render("home.jsp");
	}
	
	@Clear(AuthorityInterceptor.class)
	public void login() {
		this.setAttr("url", this.getPara("url"));
		render("login.jsp");
	}
	@Clear(AuthorityInterceptor.class)
	public void dologin() {
		String imageCode=this.getPara("imageCode");
		if(StrKit.notBlank(imageCode)){
			String imageCodeSession=(String)this.getSessionAttr("imageCode");
			if(!imageCodeSession.toLowerCase().equals(imageCode.trim().toLowerCase())){
				this.renderJson(InvokeResult.failure("验证码输入有误"));
				return;
			}
		}else{
			this.renderJson(InvokeResult.failure("请输入验证码"));
			return;
		}
		SysUser sysUser=SysUser.me.getByName(this.getPara("username"));
		if (sysUser == null) {
			sysUser = SysUser.me.getByEmail(this.getPara("username"));
			if (sysUser == null) {
				sysUser = SysUser.me.getByPhone(this.getPara("username"));
			}
		}
		if(sysUser==null){
			this.renderJson(InvokeResult.failure("用户不存在"));
			return;
		}
		if(SysLoginRecord.dao.hasOverLoginErrTimes(sysUser.getId())){
			this.renderJson(InvokeResult.failure("今天连续输入密码错误次数超过5次"));
			return;
		}
		if(!sysUser.getPwd().equals(MyDigestUtils.shaDigestForPasswrod(this.getPara("password")))){
			SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(),0);
			this.renderJson(InvokeResult.failure("用户密码输入有误"));
			return;
		}
		if(sysUser.getInt("status")==2){
			this.renderJson(InvokeResult.failure("用户被冻结，请联系管理员"));
			return;
		}
		Integer autoLogin=this.getParaToInt("autoLogin",0);
		IWebUtils.setCurrentLoginSysUser(this.getResponse(),this.getSession(),sysUser,autoLogin);
		SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(),1);
		this.renderJson(InvokeResult.success());
	}
	
	public void loginOut() {
		IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
		this.redirect("/login");
	}
	
	public void pwdSetting(){
		try {
			SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
			this.setAttr("sysUser", sysUser);
			render("sys/pwd_setting.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void savePwdUpdate(){
		String oldPwd=MyDigestUtils.shaDigestForPasswrod(getPara("oldPwd"));
		String newPwd=MyDigestUtils.shaDigestForPasswrod(getPara("newPwd"));
		String pwdRepeat=MyDigestUtils.shaDigestForPasswrod(getPara("pwdRepeat"));
		try {
			SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
			if(!sysUser.get("pwd").equals(oldPwd)){
				this.renderJson(InvokeResult.failure(-3, "旧密码不正确"));
			}else{
				if(newPwd.equals(pwdRepeat)){
					InvokeResult result=SysUser.me.savePwdUpdate(sysUser.getInt("id"), newPwd);
					this.renderJson(result);
				}else{ 
					this.renderJson(InvokeResult.failure(-1, "两次密码不一致"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Clear(AuthorityInterceptor.class)
    public void regist(){
        IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
        String username=this.getPara("username");
        String password=this.getPara("password");
        String phone=this.getPara("phone");
        String email=this.getPara("email");
        InvokeResult result=SysUser.me.regist(username, password, phone, email);
        SysUser sysUser=SysUser.me.getByName(username);
        IWebUtils.setCurrentLoginSysUser(this.getResponse(),this.getSession(),sysUser,0);
        this.renderJson(result);
    }

	@Clear(AuthorityInterceptor.class)
	public void resetPwdSendEmail(){
		IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
		String email=this.getPara("email");
		String host = PropKit.get("config.mail.host");		// 发送邮件的服务器的IP
		String port = PropKit.get("config.mail.port");	// 发送邮件的服务器的端口

		boolean validate = true;	// 是否需要身份验证

		String from = PropKit.get("config.mail.from");		// 邮件发送者的地址
		String userName = PropKit.get("config.mail.userName");	// 登陆邮件发送服务器的用户名
		String password = PropKit.get("config.mail.password");	// 登陆邮件发送服务器的密码

		List<String> to = new ArrayList<String>();			// 邮件接收者的地址
		to.add(email);

		String subject = "通州水务管理系统-找回密码";		// 邮件标题
		StringBuilder content = new StringBuilder("<h2>请点击链接重置密码</h2>");// 邮件的文本内容
		content.append("<br/>");
		content.append("<a href='");
		content.append("config.host.url");
		content.append("/login");
		content.append("?email=" + email);
		content.append("&time="+ System.currentTimeMillis());
		content.append("' target='_blank' >重置密码(30分钟内有效)</a>");

		String[] attachFileNames = new String[]{""};	// 邮件附件的文件名

		ToolMail.sendHtmlMail(host, port, validate, userName, password, from, to, subject, content.toString(), attachFileNames);

		if(log.isErrorEnabled()) {
			log.error("发送邮件参数sendType错误");
		}

		this.renderJson(InvokeResult.success());
	}

	@Clear(AuthorityInterceptor.class)
	public void savePwdForget(){
		String email=this.getPara("email");
		String newPwd=MyDigestUtils.shaDigestForPasswrod(getPara("password"));
		try {
			SysUser sysUser = SysUser.me.getByEmail(email);
			if(sysUser==null){
				this.renderJson(InvokeResult.failure("邮箱不存在"));
				return;
			}
			InvokeResult result=SysUser.me.savePwdUpdate(sysUser.getInt("id"), newPwd);
			this.renderJson(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}





