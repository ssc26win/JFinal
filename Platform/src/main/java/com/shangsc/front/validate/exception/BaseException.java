package com.shangsc.front.validate.exception;


import com.shangsc.front.validate.error.ErrorCode;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -3459644439460132051L;

    protected ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BaseException(Exception e, ErrorCode errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

    public BaseException(String msg, Exception e, ErrorCode errorCode) {
        super(msg, e);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        String msg = builder.append(super.getMessage() == null ? "" : super.getMessage()).append(this.errorCode.getComment()).toString();
        return msg;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public int getErrorCodeValue() {
        return this.errorCode.getValue();
    }

    protected void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


}
