package com.shangsc.front.validate.error;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public enum BasicErrorCode implements ErrorCode {

    BASIC_INPUT_PARAM_TRIM(100, "输入参数不能为空"),
    BASIC_INPUT_PARAM_MIN(101, "输入参数长度过短"),
    BASIC_INPUT_PARAM_MAX(102, "输入参数长度过长"),
    BASIC_INPUT_PARAM_NUMBER(103, "输入参数不为数字"),
    BASIC_INPUT_PARAM_DATE(104, "输入参数不为日期格式"),
    BASIC_INPUT_PARAM_DATETIME(105, "时间输入不合法"),
    BASIC_INPUT_PARAM_STARTDATETIME(106, "开始时间输入不合法"),
    BASIC_INPUT_PARAM_ENDDATETIME(107, "结束时间输入不合法"),
    BASIC_INPUT_PARAM_INVALIDATE(108, "结果值不在范围内"),
    BASIC_INPUT_PARAM_ERROR(109, "参数值不合法"),
    BASIC_INPUT_PARAM_REQUESTID(110, "requestId为空"),
    BASIC_OUTPUT_PARAM_ERROR(111, "输出参数值不合法"),
    BASIC_DB_ERRORCODE(112, "数据库错误"),
    BASIC_REDIS_ERRORCODE(113, "redis错误"),

    BASIC_DB_CONSISTENCE(114, "违反数据库唯一性约束"),
    BASIC_NO_SUCHDATA(115, "无此记录"),
    UNKNOWN(-1, "系统开小差了，请您稍后再试");


    private final int value;
    private final String comment;

    private BasicErrorCode(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getComment() {
        return this.comment;
    }


    private static Map<Integer, ErrorCode> map =
            new TreeMap<Integer, ErrorCode>();

    static {
        for (ErrorCode code : values()) {
            map.put(code.getValue(), code);
        }
    }

    public static ErrorCode errorCodeFor(int value) {
        return map.get(value);
    }

    public static void addNewErrorCodes(ErrorCode[] codes) {
        for (ErrorCode code : codes) {
            if (!map.containsKey(code.getValue())) {
                map.put(code.getValue(), code);
            }
        }
    }

}
