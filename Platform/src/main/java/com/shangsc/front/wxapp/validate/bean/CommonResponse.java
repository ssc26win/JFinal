package com.shangsc.front.wxapp.validate.bean;

import com.shangsc.front.wxapp.validate.error.ErrorCode;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class CommonResponse<T> extends CommonDes {

    private static final long serialVersionUID = 7253918738347134415L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    @SuppressWarnings("unchecked")
    public static <T> CommonResponse<T> success(T data)
    {
        CommonResponse commonReturn = new CommonResponse();
        commonReturn.setData(data);
        return  commonReturn;
    }
    public static CommonResponse fail(ErrorCode errorCode)
    {
        CommonResponse commonReturn = new CommonResponse();
        commonReturn.setCode(errorCode.getValue());
        commonReturn.setMessage(errorCode.getComment());
        return  commonReturn;
    }
}