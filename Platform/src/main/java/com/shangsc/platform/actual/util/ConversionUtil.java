package com.shangsc.platform.actual.util;

import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Author ssc
 * @Date 2017/9/9 18:48
 * @Version 1.0.0
 * @Desc
 */
public class ConversionUtil {
    /**
     * @Title:bytes2HexString
     * @Description:字节数组转16进制字符串
     * @param b 字节数组
     * @return 16进制字符串
     * @throws
     */
    public static String bytes2Hex16Str(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    /**
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     * @param src
     *            16进制字符串
     * @return 字节数组
     * @throws
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     * @param strPart
     *            字符串
     * @return 16进制字符串
     * @throws
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hex16Str2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return temp;
    }

    /**
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     * @param src
     * @return
     * @throws
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int)src).byteValue();
    }

    /**
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     * @param a 转化数据
     * @param len 占用字节数
     * @return
     * @throws
     */
    private static String intToHexString(int a,int len){
        len<<=1;
        String hexString = Integer.toHexString(a);
        int b = len -hexString.length();
        if(b>0){
            for(int i=0;i<b;i++)  {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private static final String CUT = "FEFEFE";
    private static final String T2C = "2C";

    public static String getMeterNum(String target) {
        try {
            if (StringUtils.isNotEmpty(target)) {
                String[] array = target.split(CUT);
                if (array.length > 0) {
                    String source16 = array[0];
                    String source = hex16Str2String(source16);
                    if (source.indexOf("&") > 0) {
                        String[] arrayNum = source.split("&");
                        String numSrc = arrayNum.length > 0 ? arrayNum[1] : "";
                        return numSrc;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static BigDecimal getMeterSum(String target) {
        try {
            if (StringUtils.isNotEmpty(target)) {
                String numStr = target.substring(76, 96);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[0];
                        StringBuffer sb = new StringBuffer("");
                        char[] chars = numSrc.toCharArray();
                        sb.append(chars[6]);
                        sb.append(chars[7]);
                        sb.append(chars[4]);
                        sb.append(chars[5]);
                        sb.append(chars[2]);
                        sb.append(chars[3]);
                        sb.append(".");
                        sb.append(chars[0]);
                        sb.append(chars[1]);
                        return CodeNumUtil.getBigDecimal(sb.toString(), 2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0.00);
    }

    public static BigDecimal getMeterAdd(String target) {
        try {
            if (StringUtils.isNotEmpty(target)) {
                String numStr = target.substring(76, 96);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[1];
                        StringBuffer sb = new StringBuffer("");
                        char[] chars = numSrc.toCharArray();
                        sb.append(chars[6]);
                        sb.append(chars[7]);
                        sb.append(chars[4]);
                        sb.append(chars[5]);
                        sb.append(chars[2]);
                        sb.append(chars[3]);
                        sb.append(".");
                        sb.append(chars[0]);
                        sb.append(chars[1]);
                        return CodeNumUtil.getBigDecimal(sb.toString(), 2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0.00);
    }

    public static String getLineNum(String target) {
        try {
            if (StringUtils.isNotEmpty(target)) {
                return target.substring(52, 66);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void main(String args[]) {
        String target = "4354524C3A26323031373037303030303030393231" +
                "FEFEFE" +
                "68" +
                "10" +
                "21090000170002" +
                "81" +
                "16" +
                "901F" +
                "00" +
                "00020000" +
                "2C" +
                "00000000" +
                "2C" +
                "00000000000000" +
                "0000" +
                "5B" +
                "16";
        System.out.println("4354524C3A26323031373037303030303030393231".length());

        System.out.println(target.length());

        System.out.println(ConversionUtil.hex16Str2String("4354524C3A26323031373037303030303030393231"));

        System.out.println(ConversionUtil.hex16Str2String("FEFEFE"));

        System.out.println(Integer.parseInt("00020000", 16));

        System.out.println(Integer.parseInt("00000000", 16));

        System.out.println(getMeterNum(target));

        System.out.println(getMeterSum(target));

        System.out.println(getMeterAdd(target));

        System.out.println(target.substring(52, 66));
    }
}
