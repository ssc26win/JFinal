package com.shangsc.front.wxapp.validate.exception;

import com.shangsc.front.wxapp.validate.error.BasicErrorCode;
import com.shangsc.front.wxapp.validate.error.ErrorCode;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class UnknownException extends BaseException {

    private static final long serialVersionUID = 4791183252468885498L;

    private static final ErrorCode errorCode = BasicErrorCode.errorCodeFor(-1);

    public UnknownException(Exception e) {
        super(e, errorCode);
    }

    public UnknownException(String msg) {
        super(msg, errorCode);
    }

    public UnknownException(String msg, Exception e) {
        super(msg, e, errorCode);
    }

}
