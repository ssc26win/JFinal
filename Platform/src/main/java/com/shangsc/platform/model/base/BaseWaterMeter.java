package com.shangsc.platform.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.shangsc.platform.core.model.BaseModel;

import java.math.BigDecimal;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWaterMeter<M extends BaseWaterMeter<M>> extends BaseModel<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setInnerCode(String innerCode) {
		set("inner_code", innerCode);
	}

	public String getInnerCode() {
		return get("inner_code");
	}

	public void setLineNum(String lineNum) {
		set("line_num", lineNum);
	}

	public String getLineNum() {
		return get("line_num");
	}

	public void setMeterNum(String meterNum) {
		set("meter_num", meterNum);
	}

	public String getMeterNum() {
		return get("meter_num");
	}

	public void setMeterAddress(String meterAddress) {
		set("meter_address", meterAddress);
	}

	public String getMeterAddress() {
		return get("meter_address");
	}

	public void setTimes(BigDecimal times) {
		set("times", times);
	}

	public BigDecimal getTimes() {
		return get("times");
	}

	public void setWatersType(Integer watersType) {
		set("waters_type", watersType);
	}

	public Integer getWatersType() {
		return get("waters_type");
	}

	public void setMeterAttr(String meterAttr) {
		set("meter_attr", meterAttr);
	}

	public String getMeterAttr() {
		return get("meter_attr");
	}

	public void setChargeType(Integer chargeType) {
		set("charge_type", chargeType);
	}

	public Integer getChargeType() {
		return get("charge_type");
	}

	public void setBillingCycle(String billingCycle) {
		set("billing_cycle", billingCycle);
	}

	public String getBillingCycle() {
		return get("billing_cycle");
	}

	public void setRegistDate(java.util.Date registDate) {
		set("regist_date", registDate);
	}

	public java.util.Date getRegistDate() {
		return get("regist_date");
	}

}
