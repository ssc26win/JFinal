package com.shangsc.platform.model.base;

import com.shangsc.platform.core.model.BaseModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseActualLog<M extends BaseActualLog<M>> extends BaseModel<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setSrcType(String srcType) {
		set("src_type", srcType);
	}

	public String getSrcType() {
		return get("src_type");
	}

	public void setPort(Integer port) {
		set("port", port);
	}

	public Integer getPort() {
		return get("port");
	}

	public void setIp(String ip) {
		set("ip", ip);
	}

	public String getIp() {
		return get("ip");
	}

	public void setContent(String content) {
		set("content", content);
	}

	public String getContent() {
		return get("content");
	}

	public void setAddTime(java.util.Date addTime) {
		set("add_time", addTime);
	}

	public java.util.Date getAddTime() {
		return get("add_time");
	}

}
