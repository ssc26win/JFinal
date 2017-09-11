package com.shangsc.platform.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/9/11 19:55
 * @Desc 用途：
 */
public class MonthCode {

    public static final Integer january = 1;
    public static final Integer february = 2;
    public static final Integer march = 3;
    public static final Integer april = 4;
    public static final Integer may = 5;
    public static final Integer june = 6;
    public static final Integer july = 7;
    public static final Integer august = 8;
    public static final Integer september = 9;
    public static final Integer october = 10;
    public static final Integer november = 11;
    public static final Integer december = 12;

    public static final String january_str = "january";
    public static final String february_str = "february";
    public static final String march_str = "march";
    public static final String april_str = "april";
    public static final String may_str = "may";
    public static final String june_str = "june";
    public static final String july_str = "july";
    public static final String august_str = "august";
    public static final String september_str = "september";
    public static final String october_str = "october";
    public static final String november_str = "november";
    public static final String december_str = "december";

    public static Map<Integer, String> getMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(january, january_str);
        map.put(february, february_str);
        map.put(march, march_str);
        map.put(april, april_str);
        map.put(may, may_str);
        map.put(june, june_str);
        map.put(july, july_str);
        map.put(august, august_str);
        map.put(september, september_str);
        map.put(october, october_str);
        map.put(november, november_str);
        map.put(december, december_str);
        return map;
    }
}
