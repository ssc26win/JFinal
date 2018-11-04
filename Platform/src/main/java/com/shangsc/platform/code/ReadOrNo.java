package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

public class ReadOrNo {

    public static final String CODETYPE = "ReadOrNo";
    public static final String Read = "1";
    public static final String No = "0";
    public static final String Delete = "2";

    public static final String ReadColorStr = "<span style='color:green;'>已读</span>";
    public static final String NoColorStr = "<span style='color:red;'>未读</span>";
    public static final String DeleteColorStr = "<span style='color:gray;'>已删除</span>";

    public static final String ReadStr = "已读";
    public static final String NoStr = "未读";
    public static final String DeleteStr = "已删除";

    public static Map<String, String> getMap() {
        Map<String, String> addType = new HashMap<String, String>();
        addType.put(Read, ReadStr);
        addType.put(No, NoStr);
        return addType;
    }

    public static Map<String, String> getColorMap() {
        Map<String, String> addType = new HashMap<String, String>();
        addType.put(Read, ReadColorStr);
        addType.put(No, NoColorStr);
        addType.put(Delete, DeleteColorStr);
        return addType;
    }

    public static boolean isRead(String str) {
        return ReadStr.equals(str);
    }

    public static boolean isNo(String str) {
        return !isRead(str);
    }

}
