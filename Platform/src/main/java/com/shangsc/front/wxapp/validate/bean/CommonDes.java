package com.shangsc.front.wxapp.validate.bean;

import com.shangsc.front.wxapp.util.JsonUtil;
import com.shangsc.front.wxapp.validate.error.BasicErrorCode;
import com.shangsc.front.wxapp.validate.exception.BaseException;
import com.shangsc.front.wxapp.validate.exception.UnknownException;

import java.io.Serializable;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class CommonDes implements Serializable {

    private static final long serialVersionUID = -1273191001397763057L;

    private int code;
    private String message = "success";

    /**
     * 判断返回结果是否成功
     */
    public boolean success() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 转换或复制错误结果
     */
    public void setError(CommonDes failure) {
        //   CommonDes result = new CommonDes();
        code = failure.getCode() != 0 ? failure.getCode() : -1;
        if (code != -1) {
            message = failure.getMessage();
        } else {
            message = BasicErrorCode.errorCodeFor(code).getComment();
        }
    }

    @Override
    public String toString() {
        return JsonUtil.obj2Json(this);
    }

    /**
     * 异常 转换
     * 将baseExcetpion 转换为 CommonDes 结果返回
     *
     * @param exception
     * @return
     */
    public static CommonDes getBaseResponse(RuntimeException exception) {
        BaseException baseException;
        if (exception instanceof BaseException) {
            baseException = (BaseException) exception;
        } else {
            baseException = new UnknownException(exception);
        }

        CommonDes exceptionDes = new CommonDes();

        exceptionDes.setCode(baseException.getErrorCodeValue());
        exceptionDes.setMessage(baseException.getMessage());

        return exceptionDes;
    }

    public static CommonDes makeErrorResult() {
        return new CommonDes();
    }
}

