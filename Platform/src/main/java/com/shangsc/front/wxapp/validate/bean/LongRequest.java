package com.shangsc.front.wxapp.validate.bean;

import javax.validation.constraints.Min;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class LongRequest extends BaseRequest {

    private static final long serialVersionUID = -6817763231819898028L;

    @Min(1)
    Long id;

    public LongRequest() {
    }

    public LongRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
