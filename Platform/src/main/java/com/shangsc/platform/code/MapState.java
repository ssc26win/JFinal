package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/9/11 18:24
 * @Desc 用途：
 */
public class MapState {
    public static final Integer NORMAL = 0;
    public static final Integer WARN = 1;
    public static final Integer OVER = 2;

    public static final String NORMAL_STR = "0";
    public static final String WARN_STR = "1";
    public static final String OVER_STR = "2";

    public static Map<Integer, String> getMap() {
        Map<Integer, String> actualStateMap = new HashMap<Integer, String>();
        actualStateMap.put(NORMAL, NORMAL_STR);
        actualStateMap.put(WARN, WARN_STR);
        actualStateMap.put(OVER, OVER_STR);
        return actualStateMap;
    }

}
