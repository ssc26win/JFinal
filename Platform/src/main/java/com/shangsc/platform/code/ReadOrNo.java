package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

public class ReadOrNo {

    public static final String CODETYPE = "ReadOrNo";
    public static final String Read = "1";
    public static final String No = "0";

    public static final String ReadStr = "<span style='color:green;'>已读</span>";
    public static final String NoStr = "<span style='color:red;'>未读</span>";

    public static Map<String, String> getMap() {
        Map<String, String> addType = new HashMap<String, String>();
        addType.put(Read, ReadStr);
        addType.put(No, NoStr);
        return addType;
    }

    public static boolean isRead(String str) {
        return ReadStr.equals(str);
    }

    public static boolean isNo(String str) {
        return !isRead(str);
    }

}
