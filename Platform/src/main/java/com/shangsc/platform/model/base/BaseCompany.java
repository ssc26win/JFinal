package com.shangsc.platform.model.base;

import com.shangsc.platform.core.model.BaseModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCompany<M extends BaseCompany<M>> extends BaseModel<M> implements IBean {

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

	public void setName(String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setWaterUnit(String waterUnit) {
		set("water_unit", waterUnit);
	}

	public String getWaterUnit() {
		return get("water_unit");
	}

	public void setCounty(String county) {
		set("county", county);
	}

	public String getCounty() {
		return get("county");
	}

	public void setStreet(Integer street) {
		set("street", street);
	}

	public Integer getStreet() {
		return get("street");
	}

	public void setStreetSrc(String streetSrc) {
		set("street_src", streetSrc);
	}

	public String getStreetSrc() {
		return get("street_src");
	}

	public void setAddress(String address) {
		set("address", address);
	}

	public String getAddress() {
		return get("address");
	}

	public void setCustomerType(Integer customerType) {
		set("customer_type", customerType);
	}

	public Integer getCustomerType() {
		return get("customer_type");
	}

	public void setGbIndustry(String gbIndustry) {
		set("gb_industry", gbIndustry);
	}

	public String getGbIndustry() {
		return get("gb_industry");
	}

	public void setMainIndustry(String mainIndustry) {
		set("main_industry", mainIndustry);
	}

	public String getMainIndustry() {
		return get("main_industry");
	}

	public void setWaterUseType(Integer waterUseType) {
		set("water_use_type", waterUseType);
	}

	public Integer getWaterUseType() {
		return get("water_use_type");
	}

	public void setContact(String contact) {
		set("contact", contact);
	}

	public String getContact() {
		return get("contact");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}

	public String getPhone() {
		return get("phone");
	}

	public void setPostalCode(String postalCode) {
		set("postal_code", postalCode);
	}

	public String getPostalCode() {
		return get("postal_code");
	}

	public void setDepartment(String department) {
		set("department", department);
	}

	public String getDepartment() {
		return get("department");
	}

	public void setWellCount(Integer wellCount) {
		set("well_count", wellCount);
	}

	public Integer getWellCount() {
		return get("well_count");
	}

	public void setFirstWatermeterCount(Integer firstWatermeterCount) {
		set("first_watermeter_count", firstWatermeterCount);
	}

	public Integer getFirstWatermeterCount() {
		return get("first_watermeter_count");
	}

	public void setRemotemeterCount(Integer remotemeterCount) {
		set("remotemeter_count", remotemeterCount);
	}

	public Integer getRemotemeterCount() {
		return get("remotemeter_count");
	}

	public void setUnitType(Integer unitType) {
		set("unit_type", unitType);
	}

	public Integer getUnitType() {
		return get("unit_type");
	}

	public void setLongitude(java.math.BigDecimal longitude) {
		set("longitude", longitude);
	}

	public java.math.BigDecimal getLongitude() {
		return get("longitude");
	}

	public void setLatitude(java.math.BigDecimal latitude) {
		set("latitude", latitude);
	}

	public java.math.BigDecimal getLatitude() {
		return get("latitude");
	}

	public void setSelfWellPrice(java.math.BigDecimal selfWellPrice) {
		set("self_well_price", selfWellPrice);
	}

	public java.math.BigDecimal getSelfWellPrice() {
		return get("self_well_price");
	}

	public void setSurfacePrice(java.math.BigDecimal surfacePrice) {
		set("surface_price", surfacePrice);
	}

	public java.math.BigDecimal getSurfacePrice() {
		return get("surface_price");
	}

	public void setSelfFreePrice(java.math.BigDecimal selfFreePrice) {
		set("self_free_price", selfFreePrice);
	}

	public java.math.BigDecimal getSelfFreePrice() {
		return get("self_free_price");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
