package com.shangsc.platform.actual.util;

import com.shangsc.platform.code.TcpData;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author ssc
 * @Date 2018/4/23 17:30
 * @Desc 用途：
 */
public class TcpConvertUtil implements Serializable {
    private static final long serialVersionUID = -8522245588226692136L;

    public static final Logger logger = LoggerFactory.getLogger(TcpConvertUtil.class);

    public static final String Tcp_startStr = "6809006800";
    //public static final String Tcp_RESP_LOGIN = "00";
    //public static final String Tcp_RESP_DATA = "4B";
    private static final String AFN = "00";
    private static final String SEQ = "60";
    private static final String FN = "01";
    private static final String END = "16";

    public static final String[] Tcp_startArr = new String[]{"68", "09", "00", "68", "00"};

    public static String tcpLoginResp(String result, String type) {
        String meterAddress = result.substring(10, 20);
        String middle = meterAddress + AFN + SEQ + FN;
        String chkStr = getTcpCheckStr(to2StrArray(middle)).toUpperCase();
        logger.info(type + " check code : " + middle + " return : " + chkStr);
        String resp = Tcp_startStr + middle + chkStr + END;
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
        return Long.toHexString((total) % 256);
    }

    public static String[] to2StrArray(String result) {
        int length = result.length() / 2 + 1;
        String[] demo = new String[length];
        char[] target = result.toCharArray();
        int j = 0;
        for (int i = 0; i < target.length; i++) {
            if (CodeNumUtil.isOdd(i)) {
                demo[j] = String.valueOf(target[i - 1]) + String.valueOf(target[i]);
                j++;
            }
        }
        return demo;
    }

    public static String receiveDataResp(String result) {
        String upload_data = "";
        if (result.length() == TcpData.upload_data_length_1) {
            upload_data = result.substring(26, 26 + 64);
        } else if (result.length() == TcpData.upload_data_length_2) {
            upload_data = result.substring(26 + 12, 26 + 12 + 64);
        } else {
            logger.warn("getTcpSumNum() 未知类型 result={}", result);
        }
        String respStr = result.replace(upload_data, "");
        String resp = tcpLoginResp(respStr, "receiveData");
        return resp;
    }

    public static String getTcpMeterAddress(String result) {
        return result.substring(10, 20);
    }

    public static BigDecimal getTcpSumNum(String result) {
        String upload_data = "";
        if (result.length() == TcpData.upload_data_length_1) {
            upload_data = result.substring(26, 26 + 64);
        } else if (result.length() == TcpData.upload_data_length_2) {
            upload_data = result.substring(26 + 12, 26 + 12 + 64);
        } else {
            logger.warn("getTcpSumNum() 未知类型 result={}", result);
        }
        String sumNumStr = upload_data.substring(16, 24);
        String respData = tcpStupidBCD(sumNumStr);
        String pointData = getTcpPointNum(result);
        BigDecimal all = CodeNumUtil.getBigDecimal(respData + "." + pointData, 2);
        if (result.length() == TcpData.upload_data_length_2) {
            all = all.multiply(new BigDecimal(100));
        }
        return all;
    }

    public static String getTcpPointNum(String result) {
        String upload_data = "";
        if (result.length() == TcpData.upload_data_length_1) {
            upload_data = result.substring(26, 26 + 64);
        } else if (result.length() == TcpData.upload_data_length_2) {
            upload_data = result.substring(26 + 12, 26 + 12 + 64);
        } else {
            logger.warn("getTcpSumNum() 未知类型 result={}", result);
        }
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

    public static String getVoltage() {
        return "";
    }

    public static String getTcpMultAddress(String result) {
        return result.substring(10, 20);
    }

    public static BigDecimal getTcpMultRecordSumWater(String result) {
        String substring = result.substring(24, 26);
        logger.info("Multi FN : {}", substring);
        if ((result.length() - 42) % 64 == 0 && TcpData.TCP_FN_2.equals(substring)) {
            return getTcpMult2RecordSumWater(result);
        } else if (TcpData.TCP_FN_1.equals(substring)) {
            return getTcpMult1RecordSumWater(result);
        } else {
            return getTcpMult1RecordSumWater(result);
        }
    }

    public static BigDecimal getTcpMult1RecordSumWater(String result) {
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

    public static BigDecimal getTcpMult2RecordSumWater(String result) {
        String lastRecord = result.substring(result.length() - 68, result.length());
        logger.info("Multi last record:" + lastRecord);
        //String sumNumStr = result.substring(26 + 8 * 2, 26 + 12 * 2);
        String sumNumStr = lastRecord.substring(16, 24);
        logger.info("Multi sumNumStr:" + sumNumStr);
        String respDataSum = tcpStupidBCD(sumNumStr);
        //String pointNumStr = result.substring(26 + 12 * 2, 26 + 16 * 2);
        String pointNumStr = lastRecord.substring(24, 32);
        logger.info("Multi pointNumStr:" + pointNumStr);
        String respDataPoint = tcpStupidBCD(pointNumStr);
        BigDecimal all = CodeNumUtil.getBigDecimal(respDataSum + "." + respDataPoint, 2);
        BigDecimal finalRes = all.multiply(new BigDecimal(100));
        return finalRes;
    }

    public static String getTcpMultChkStr(String result) {
        return tcpLoginResp(result, "Multi");
    }


    public static String aa = "683F0068CA17071600020D7004E891" +
            "BB596400000000000000000000AE29004094" +
            "BB596400000000000000000000AE2900" +
            "9896BB596400000000000000000000942900CF16";

    public static String bb = "68CF0368CA18061201470D100723917856341238A66F5B000000009C0200000000000000000000000000000000000000E42900BCA96F5B000000009C0200000000000000000000000000000000000000E4290040AD6F5B000000009C0200000000000000000000000000000000000000F02900C4B06F5B000000009C0200000000000000000000000000000000000000A1290048B46F5B000000009C0200000000000000000000000000000000000000D32900CCB76F5B000000009C0200000000000000000000000000000000000000CB290050BB6F5B000000009C0200000000000000000000000000000000000000D72900D4BE6F5B000000009C0200000000000000000000000000000000000000D7290058C26F5B000000009C0200000000000000000000000000000000000000E42900DCC56F5B000000009C0200000000000000000000000000000000000000E0290060C96F5B000000009C0200000000000000000000000000000000000000E82900E4CC6F5B000000009C0200000000000000000000000000000000000000A1290068D06F5B000000009C0200000000000000000000000000000000000000CF2900ECD36F5B000000009C0200000000000000000000000000000000000000D7290070D76F5B000000009C0200000000000000000000000000000000000000CB2900F4DA6F5B000000009C0200000000000000000000000000000000000000D3290078DE6F5B000000009C0200000000000000000000000000000000000000D72900FCE16F5B000000009C0200000000000000000000000000000000000000D7290080E56F5B000000009C0200000000000000000000000000000000000000E4290004E96F5B000000009C0200000000000000000000000000000000000000A5290088EC6F5B000000009C0200000000000000000000000000000000000000C729000CF06F5B000000009C0200000000000000000000000000000000000000D3290090F36F5B000000009C0200000000000000000000000000000000000000CF290014F76F5B000000009C0200000000000000000000000000000000000000D3290098FA6F5B000000009C0200000000000000000000000000000000000000DB29001CFE6F5B000000009C0200000000000000000000000000000000000000DB2900A001705B000000009C0200000000000000000000000000000000000000E029002405705B000000009C02000000000000000000000000000000000000009D2900A808705B000000009C0200000000000000000000000000000000000000CB29002C0C70" +
            "5B000000009C0200000000" +
            "000000000000000000000000000000CB2900E316";


    public static final String upload_data_1 = "682D0068CA17071600070D7004489EC75970FF030000000000000000A737000847C859280B0400000000000000008736004016";

    public static final String upload_data_2 = "682F0068CA18061500180D7007239178563412046D735B00000000DB2400000000000000000000000000000000000000B629007E16";


    public static void main(String[] args) {
        System.out.println(aa);
       // System.out.println(getTcpMultRecordSumWater(aa));
        //receiveDataResp(aa);

        tcpLoginResp(upload_data_1, "login");
        //
        //tcpLoginResp(upload_data_2, "login");

        getTcpMultChkStr(aa);


        System.out.println(bb);

        getTcpMultChkStr(bb);

        System.out.println(getTcpSumNum("682F0068CA18061500150D700723917856341204BEAE3800000000603A000000000000000000000000000000000000002F2A00F916"));

    }

    public static void main2(String args[]) {


//        System.out.println(getTcpSumNum("682F0068CA18061500180D7007239178563412046D735B00000000DB2400000000000000000000000000000000000000B629007E16"));
//
        System.out.println(aa);
        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println(getTcpMultRecordSumWater(bb));
        System.out.println(getTcpMultAddress(bb));

        System.out.println("last record:" + aa.substring(aa.length() - 40, aa.length()));

        System.out.println(aa.substring(aa.length() - 40, aa.length()).substring(8, 16));
        System.out.println(aa.substring(aa.length() - 40, aa.length()).substring(16, 24));


        System.out.println(getTcpMultChkStr(aa));

        System.out.println(getTcpMultRecordSumWater(aa));

        System.out.println(ConversionUtil.hexString2Bytes(aa)[9] + ConversionUtil.hexString2Bytes(aa)[10]
                + ConversionUtil.hexString2Bytes(aa)[11] + ConversionUtil.hexString2Bytes(aa)[12]);

        System.out.println(aa.substring(26 + 12 * 2, 26 + 16 * 2));

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

        System.out.println(Long.toHexString((x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 + x12) % 256));


        // System.out.println(hexString2Bytes("68090068C91707160002027001"));


        String upload_data = "584D594E30303030303030303030333030313735323532370D0A";


    }

}
