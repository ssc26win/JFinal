package com.shangsc.platform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author ssc
 * @Date 2018/10/24 11:24
 * @Desc 用途：
 */
public class URLUtil {

    public static String encoder(String str) {//url编码
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decoder(String str) {//url解码
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(decoder("\u0094\u00AD\u0094\u0085\u008D\u0081\u009A"));
    }
}
