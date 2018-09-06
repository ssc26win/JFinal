
package com.shangsc.platform.code;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum ReportTypeEnum {

    STREET(0, "所属乡镇"),
    YEAR(1, "年"),
    MONTH(2, "月"),
    DAY(3, "日");

    private int code;
    private String nick;


    public int getCode() {
        return code;
    }

    public String getNick() {
        return nick;
    }

    private ReportTypeEnum(int code, String nick) {
        this.code = code;
        this.nick = nick;

    }

    @Override
    public String toString() {
        return code + "";
    }

    private static EnumSet<ReportTypeEnum> setAll = EnumSet.allOf(ReportTypeEnum.class);

    private static Map<Integer, ReportTypeEnum> mapAll = new HashMap<Integer, ReportTypeEnum>();

    static {
        for (ReportTypeEnum item : setAll) {
            mapAll.put(item.getCode(), item);
        }
    }

    private static Map<String, ReportTypeEnum> mapAll2 = new HashMap<String, ReportTypeEnum>();

    static {
        for (ReportTypeEnum item : setAll) {
            mapAll2.put(item.getNick(), item);
        }
    }

    public static ReportTypeEnum getByCode(int key) {
        return mapAll.get(key);
    }

    public static ReportTypeEnum getByNick(String nick) {
        return mapAll2.get(nick);
    }

}
