package com.shangsc.platform.util;

import com.shangsc.platform.model.Company;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @Author ssc
 * @Date 2017/8/20 17:06
 * @Version 1.0.0
 * @Desc
 */
public class CodeNumUtil {

    public static final String COMPANY_CODE_PREFIX = "SJ";

    /**
     * 是奇数
     *
     * @param a
     * @return
     */
    public static boolean isOdd(int a) {
        if (a % 2 == 1) {
            return true;
        }
        return false;
    }

    public static BigDecimal getBigDecimal(String target, int subScale) {
        Double f = Double.parseDouble(target);
        BigDecimal b = new BigDecimal(f);
        b = b.setScale(subScale, BigDecimal.ROUND_HALF_UP);
        return b;
    }

    public static String genInnerCode() {
        String result = COMPANY_CODE_PREFIX + get6Random();
        while (Company.me.findByInnerCode(result) != null) {
            result = COMPANY_CODE_PREFIX + get6Random();
        }
        return result;
    }

    public static String genInnerCode2(int j) {
        String result = COMPANY_CODE_PREFIX + String.format("%06d", j);
        return result;
    }

    public static String get6Random() {
        String result = "";
        while (result.length() < 6) {
            String str = String.valueOf((int) (Math.random() * 10));
            if (result.indexOf(str) == -1) {
                result += str;
            }
            if (result.length() == 6) {
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        long randomNum = System.currentTimeMillis();
        System.out.println(genInnerCode());
        System.out.println(isOdd(2));
        System.out.println(genInnerCode2(0));
        System.out.println(get6Random());
    }
}
