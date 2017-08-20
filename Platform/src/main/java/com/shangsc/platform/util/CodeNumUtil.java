package com.shangsc.platform.util;

import java.text.NumberFormat;

/**
 * @Author ssc
 * @Date 2017/8/20 17:06
 * @Version 1.0.0
 * @Desc
 */
public class CodeNumUtil {

    public static final String COMPANY_CODE_PREFIX = "0001";

    public static String genInnerCode(int i) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(8);
        String result = COMPANY_CODE_PREFIX  + format.format(i).replace(",", "");
        return result;
    }

    public static String genInnerCode2(int j) {
        String result = COMPANY_CODE_PREFIX + String.format("%06d", j);
        return result;
    }

    public static void main(String[] args) {


        System.out.println(genInnerCode(1));


        System.out.println(genInnerCode2(22332));
    }
}
