package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/9/11 17:18
 * @Desc 用途：
 */
public class ActualState {

    public static final String NORMAL = "0";
    public static final String EXCEPTION = "1";
    public static final String STOP = "2";

    public static final String NORMAL_STR = "正常";
    public static final String EXCEPTION_STR = "异常";
    public static final String STOP_STR = "停用";

    public static Map<String, String> getMap() {
        Map<String, String> actualStateMap = new HashMap<String, String>();
        actualStateMap.put(NORMAL, NORMAL_STR);
        actualStateMap.put(EXCEPTION, EXCEPTION_STR);
        actualStateMap.put(STOP, STOP_STR);
        return actualStateMap;
    }

}
