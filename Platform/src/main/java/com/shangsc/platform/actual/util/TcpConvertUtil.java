package com.shangsc.platform.actual.util;

import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @Author ssc
 * @Date 2018/4/23 17:30
 * @Desc 用途：
 */
public class TcpConvertUtil {

    public static final Logger logger = LoggerFactory.getLogger(TcpConvertUtil.class);

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
        logger.info(type + " check code : " + middle + " return : " + chkStr);
        String resp = Tcp_startStr +  middle + chkStr + END;
        logger.info(type + " resp result : " + resp);
        return resp;
    }

    public static String getTcpCheckStr(String[] array) {
        Long total = 0L;
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
        logger.info(totalStr);
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
        logger.info("Multi last record:" + lastRecord);
        //String sumNumStr = result.substring(26 + 8 * 2, 26 + 12 * 2);
        String sumNumStr = lastRecord.substring(8, 16);
        logger.info("Multi sumNumStr:" + sumNumStr);
        String respDataSum = tcpStupidBCD(sumNumStr);
        //String pointNumStr = result.substring(26 + 12 * 2, 26 + 16 * 2);
        String pointNumStr = lastRecord.substring(16, 24);
        logger.info("Multi pointNumStr:" + pointNumStr);
        String respDataPoint = tcpStupidBCD(pointNumStr);
        BigDecimal all = CodeNumUtil.getBigDecimal(respDataSum + "." + respDataPoint, 2);
        return all;
    }

    public static String getTcpMultChkStr(String result) {
        return tcpLoginResp(result, "Multi");
    }


    public static String aa = "683F0068CA17071600020D7004E891BB596400000000000000000000AE29004094BB596400000000000000000000AE29009896BB596400000000000000000000942900CF16";



    public static void main2(String args[]) {


        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println("last record:" + aa.substring(aa.length()-40, aa.length()));

        System.out.println(aa.substring(aa.length()-40, aa.length()).substring(8, 16));
        System.out.println(aa.substring(aa.length()-40, aa.length()).substring(16, 24));


        System.out.println(getTcpMultChkStr(aa));

        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println(ConversionUtil.hexString2Bytes(aa)[9] + ConversionUtil.hexString2Bytes(aa)[10]
                + ConversionUtil.hexString2Bytes(aa)[11] + ConversionUtil.hexString2Bytes(aa)[12]);

        System.out.println(aa.substring(26 + 12*2, 26 + 16*2));

        System.out.println(ConversionUtil.hex16Str2String("0B"));

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


    }

}
