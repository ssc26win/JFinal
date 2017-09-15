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

    public static String getUdpMeterAddress(String target) {
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

    public static BigDecimal getUdpMeterSum(String target) {
        try {
            if (StringUtils.isNotEmpty(target) && target.length() > 96) {
                String numStr = target.substring(76, 96);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[0];
                        return CodeNumUtil.getBigDecimal(udpStupidBCD(numSrc), 2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0.00);
    }

    public static String udpStupidBCD(String numSrc) {
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
        return sb.toString();
    }

    public static BigDecimal getUdpMeterAdd(String target) {
        try {
            if (StringUtils.isNotEmpty(target) && target.length() > 96) {
                String numStr = target.substring(76, 96);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[1];
                        return CodeNumUtil.getBigDecimal(udpStupidBCD(numSrc), 2);
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

    public static final String Tcp_startStr = "6809006800";
    private static final String AFN = "00";
    private static final String SEQ = "60";
    private static final String FN = "01";
    private static final String END = "16";

    public static final String[] Tcp_startArr = new String[] {"68","09","00","68","00"};

    public static String tcpLoginResp(String result, String type) {
        String meterAddress = result.substring(10, 20);
        String middle = meterAddress + AFN + SEQ + FN;
        String chkStr = getTcpCheckStr(to2StrArray(middle));
        System.out.println(type + " check code : " + middle + " return : " + chkStr);
        String resp = Tcp_startStr +  middle + chkStr + END;
        System.out.println(type + " resp result : " + resp);
        return resp;
    }

    public static String getTcpCheckStr(String[] array) {
        Long total = 0l;
        for (int i = 0; i < array.length; i++) {
            if (StringUtils.isNotEmpty(array[i])) {
                total = total + Long.valueOf(array[i], 16);
            }
        }
        return Long.toHexString((total)%256);
    }

    public static String[] to2StrArray(String result) {
        int length = result.length()/2 + 1;
        String[] demo = new String[length];
        char[] target = result.toCharArray();
        int j = 0 ;
        for (int i = 0; i < target.length; i++) {
            if(CodeNumUtil.isOdd(i)) {
                demo[j] = String.valueOf(target[i-1]) + String.valueOf(target[i]);
                j++;
            }
        }
        return demo;
    }

    public static String receiveDataResp(String result) {
        String upload_data = result.substring(26, 26 + 64);
        String respStr = result.replace(upload_data, "");
        String resp = tcpLoginResp(respStr, "receiveData");
        return resp;
    }

    public static String getTcpMeterAddress(String result) {
        return result.substring(10, 20);
    }

    public static BigDecimal getTcpSumNum (String result) {
        String upload_data = result.substring(26, 26 + 64);
        String sumNumStr = upload_data.substring(16, 24);
        String respData = tcpStupidBCD(sumNumStr);
        String pointData = getTcpPointNum(result);
        BigDecimal all = CodeNumUtil.getBigDecimal(respData + "." + pointData, 2);
        return all;
    }

    public static String getTcpPointNum (String result) {
        String upload_data = result.substring(26, 26 + 64);
        String pointNumStr = upload_data.substring(24, 32);
        return tcpStupidBCD(pointNumStr);
    }

    public static String tcpStupidBCD(String numSrc) {
        char[] chars = numSrc.toCharArray();
        String totalStr = String.valueOf(chars[6]) + String.valueOf(chars[7]) + String.valueOf(chars[4]) + String.valueOf(chars[5])
                + String.valueOf(chars[2]) + String.valueOf(chars[3]) + String.valueOf(chars[0]) + String.valueOf(chars[1]);
        System.out.println(totalStr);
        Long total = Long.parseLong(totalStr, 16);
        return String.valueOf(total);
    }

    public static String getVoltage(){
        return "";
    }

    public static String getTcpMultAddress(String result) {
        return result.substring(10, 20);
    }

    public static BigDecimal getTcpMultRecordSumWater(String result) {
        String lastRecord = result.substring(result.length() - 40, result.length());
        System.out.println("Multi last record:" + lastRecord);
        //String sumNumStr = result.substring(26 + 8 * 2, 26 + 12 * 2);
        String sumNumStr = lastRecord.substring(8, 16);
        System.out.println("Multi sumNumStr:" + sumNumStr);
        String respDataSum = tcpStupidBCD(sumNumStr);
        //String pointNumStr = result.substring(26 + 12 * 2, 26 + 16 * 2);
        String pointNumStr = lastRecord.substring(16, 24);
        System.out.println("Multi pointNumStr:" + pointNumStr);
        String respDataPoint = tcpStupidBCD(pointNumStr);
        BigDecimal all = CodeNumUtil.getBigDecimal(respDataSum + "." + respDataPoint, 2);
        return all;
    }

    public static String getTcpMultChkStr(String result) {
        return tcpLoginResp(result, "Multi");
    }


    public static String aa = "683F0068CA17071600020D7004E891BB596400000000000000000000AE29004094BB596400000000000000000000AE29009896BB596400000000000000000000942900CF16";

    public static void main(String args[]) {


        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println("last record:" + aa.substring(aa.length()-40, aa.length()));

        System.out.println(aa.substring(aa.length()-40, aa.length()).substring(8, 16));
        System.out.println(aa.substring(aa.length()-40, aa.length()).substring(16, 24));


        System.out.println(getTcpMultChkStr(aa));

        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println(hexString2Bytes(aa)[9] + hexString2Bytes(aa)[10] + hexString2Bytes(aa)[11] + hexString2Bytes(aa)[12]);

        System.out.println(aa.substring(26 + 12*2, 26 + 16*2));

        System.out.println(hex16Str2String("0B"));

        System.out.println(getTcpSumNum("68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216"));

        System.out.println(tcpStupidBCD("84000000"));

        System.out.println(tcpStupidBCD("3333B33E"));

        System.out.println(receiveDataResp("68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216"));


        String login_data = "68090068C9 1707160002 02 70 01 7216";
        System.out.println(login_data.length());

        System.out.println("6809006800 1707160002 00 60 01 9716".length());


        long x1 = Long.valueOf("68", 16);
        long x2 = Long.valueOf("09", 16);
        long x0 = Long.valueOf("00", 16);
        long x3 = Long.valueOf("68", 16);


        long x4 = Long.valueOf("C9", 16);
        long x5 = Long.valueOf("17", 16);
        long x6 = Long.valueOf("16", 16);
        long x7 = Long.valueOf("00", 16);
        long x8 = Long.valueOf("02", 16);

        long x9 = Long.valueOf("02", 16);
        long x10 = Long.valueOf("07", 16);
        long x11 = Long.valueOf("70", 16);
        long x12 = Long.valueOf("01", 16);

/*        char x15 = 0x72;
        char x16 = 0x16;*/

        System.out.println(Long.toHexString((x4+ x5 + x6 + x7 + x8 + x9 + x10 + x11 + x12 )%256));


       // System.out.println(hexString2Bytes("68090068C91707160002027001"));


        String upload_data = "584D594E30303030303030303030333030313735323532370D0A";


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

        System.out.println(getUdpMeterAddress(target));

        System.out.println(getUdpMeterSum(target));

        System.out.println(getUdpMeterAdd(target));

        System.out.println(target.substring(52, 66));
    }

}
