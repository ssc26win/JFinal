package com.shangsc.front.validate.bean;

import com.shangsc.front.util.JsonUtil;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class BaseRequest implements java.io.Serializable {

    private static final long serialVersionUID = 5587748028210105464L;

    @Override
    public String toString() {
        return JsonUtil.obj2Json(this);
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    private String reqId;


}
