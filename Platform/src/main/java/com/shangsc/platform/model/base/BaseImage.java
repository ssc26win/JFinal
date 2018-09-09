package com.shangsc.platform.model.base;


import com.jfinal.plugin.activerecord.IBean;
import com.shangsc.platform.core.model.BaseModel;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseImage<M extends BaseImage<M>> extends BaseModel<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setRelaId(Long relaId) {
		set("rela_id", relaId);
	}

	public Long getRelaId() {
		return get("rela_id");
	}

	public void setRelaType(String relaType) {
		set("rela_type", relaType);
	}

	public String getRelaType() {
		return get("rela_type");
	}

	public void setName(String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setOriginName(String originName) {
		set("origin_name", originName);
	}

	public String getOriginName() {
		return get("origin_name");
	}

	public void setImgUrl(String imgUrl) {
		set("img_url", imgUrl);
	}

	public String getImgUrl() {
		return get("img_url");
	}

	public void setWidth(Integer width) {
		set("width", width);
	}

	public Integer getWidth() {
		return get("width");
	}

	public void setHeight(Integer height) {
		set("height", height);
	}

	public Integer getHeight() {
		return get("height");
	}

	public void setSize(Integer size) {
		set("size", size);
	}

	public Integer getSize() {
		return get("size");
	}

	public void setMemo(String memo) {
		set("memo", memo);
	}

	public String getMemo() {
		return get("memo");
	}

	public void setUserId(Integer userId) {
		set("user_id", userId);
	}

	public Integer getUserId() {
		return get("user_id");
	}

	public void setCreateUser(String createUser) {
		set("create_user", createUser);
	}

	public String getCreateUser() {
		return get("create_user");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
