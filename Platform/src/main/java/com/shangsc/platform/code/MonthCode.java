package com.shangsc.platform.code;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public static final String warn_start_date = "start";
    public static final String warn_end_date = "end";

    public static final String warn_month = "month";
    public static final String warn_month_str="month_str";

    public static final String warn_target_month = "target_month";
    public static final String warn_target_month_str="target_month_str";

    public static List<String> monthStrList() {
        List<String> monthStrList = Arrays.asList(january_str, february_str, march_str, april_str, may_str, june_str,
                july_str, august_str, september_str, october_str, november_str, december_str);
        return monthStrList;
    }

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
