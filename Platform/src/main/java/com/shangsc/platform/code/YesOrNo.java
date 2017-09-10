package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

public class YesOrNo {

    public static final String CODETYPE = "YesOrNo";
    public static final String Yes = "1";
    public static final String No = "0";

    public static final String YesStr = "是";
    public static final String NoStr = "否";

    public static Map<String, String> getYesOrNoMap() {
        Map<String, String> addType = new HashMap<String, String>();
        addType.put(Yes, YesStr);
        addType.put(No, NoStr);
        return addType;
    }

    public static boolean isYes(String str) {
        return Yes.equals(str);
    }

    public static boolean isNo(String str) {
        return !isYes(str);
    }

}
