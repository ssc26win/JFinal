package com.shangsc.front.validate.exception;

import com.shangsc.front.validate.error.ErrorCode;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public class BizLayerException extends BaseException {

    private static final long serialVersionUID = -3308552165301022364L;

    public BizLayerException(String msg, ErrorCode errorCode) {
        super(msg, errorCode);
    }

    public BizLayerException(Exception e, ErrorCode errorCode) {
        super(e, errorCode);
    }

    public BizLayerException(String msg, Exception e, ErrorCode errorCode) {
        super(msg, e, errorCode);
    }

}
