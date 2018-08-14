package com.shangsc.platform.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.shangsc.platform.core.model.BaseModel;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysUser<M extends BaseSysUser<M>> extends BaseModel<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setName(String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setPwd(String pwd) {
		set("pwd", pwd);
	}

	public String getPwd() {
		return get("pwd");
	}

	public void setDes(String des) {
		set("des", des);
	}

	public String getDes() {
		return get("des");
	}

	public void setStatus(Integer status) {
		set("status", status);
	}

	public Integer getStatus() {
		return get("status");
	}

	public void setIcon(String icon) {
		set("icon", icon);
	}

	public String getIcon() {
		return get("icon");
	}

	public void setEmail(String email) {
		set("email", email);
	}

	public String getEmail() {
		return get("email");
	}

	public void setCreatedate(java.util.Date createdate) {
		set("createdate", createdate);
	}

	public java.util.Date getCreatedate() {
		return get("createdate");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}

	public String getPhone() {
		return get("phone");
	}

	public void setToken(String token) {
		set("token", token);
	}

	public String getToken() {
		return get("token");
	}

	public void setInnerCode(String innerCode) {
		set("inner_code", innerCode);
	}

	public String getInnerCode() {
		return get("inner_code");
	}

	public void setWxAccount(String wxAccount) {
		set("wx_account", wxAccount);
	}

	public String getWxAccount() {
		return get("wx_account");
	}

	public void setWxMemo(String wxMemo) {
		set("wx_memo", wxMemo);
	}

	public String getWxMemo() {
		return get("wx_memo");
	}

}
