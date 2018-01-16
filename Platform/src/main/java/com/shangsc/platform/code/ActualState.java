package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/9/11 17:18
 * @Desc 用途：
 */
public class ActualState {

    public static final String NORMAL = "0";
    public static final String EXCEPTION = "1";
    public static final String STOP = "2";
    public static final String DISABLE = "3";

    public static final String NORMAL_STR = "正常";
    public static final String EXCEPTION_STR = "异常";
    public static final String STOP_STR = "停用";
    public static final String DISABLE_STR = "未启用";


    public static Set<String> Actual_List() {
        Set<String> Actual_List = new HashSet<>();
        Actual_List.add(NORMAL);
        Actual_List.add(EXCEPTION);
        Actual_List.add(STOP);
        return Actual_List;
    }

    public static Map<String, String> getMap() {
        Map<String, String> actualStateMap = new HashMap<String, String>();
        actualStateMap.put(NORMAL, NORMAL_STR);
        actualStateMap.put(EXCEPTION, EXCEPTION_STR);
        actualStateMap.put(STOP, STOP_STR);
        actualStateMap.put(DISABLE, DISABLE_STR);
        return actualStateMap;
    }
}
