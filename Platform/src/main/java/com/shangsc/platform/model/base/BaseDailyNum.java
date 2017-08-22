package com.shangsc.platform.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.shangsc.platform.core.model.BaseModel;

/**
 * @Author ssc
 * @Date 2017/8/23 0:54
 * @Version 1.0.0
 * @Desc
 */
public abstract class BaseDailyNum<M extends BaseDailyNum<M>> extends BaseModel<M> implements IBean {

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

    public void setLineNum(String lineNum) {
        set("line_num", lineNum);
    }

    public String getLineNum() {
        return get("line_num");
    }

    public void setMeterNum(Integer meterNum) {
        set("meter_num", meterNum);
    }

    public Integer getMeterNum() {
        return get("meter_num");
    }

    public void setWatersType(Integer watersType) {
        set("waters_type", watersType);
    }

    public Integer getWatersType() {
        return get("waters_type");
    }

    public void setContact(String alarm) {
        set("alarm", alarm);
    }

    public String getAlarm() {
        return get("alarm");
    }

    public void setWriteTime(String writeTime) {
        set("write_time", writeTime);
    }

    public String getWriteTime() {
        return get("write_time");
    }

    public void setReadNum(String readNum) {
        set("read_num", readNum);
    }

    public String getReadNum() {
        return get("read_num");
    }

    public void setAddress(String address) {
        set("address", address);
    }

    public String getAddress() {
        return get("address");
    }

}

