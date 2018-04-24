package com.shangsc.platform.actual.util;

import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/4/23 17:31
 * @Desc 用途：
 */
public class UdpConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(UdpConvertUtil.class);

    //public static final String UDP_CUT = "FEFEFE";
    public static final String UDP_T2C = "2C";
    public static final String UDP_OTHER_HEAD = "4354524C3A";

    public static final String UDP_OTHER_END = "16";

    public static final String UDP_CUT_68 = "68";

    public static final String UDP_OTHER_CHECK = "1F9000";

    public static final int UDP_OTHER_68_16_LENGTH = 22;

    public static String getUdpMeterAddress(String target) {
        try {
            if (StringUtils.isNotEmpty(target) && target.contains(UDP_OTHER_HEAD) && target.lastIndexOf(UDP_OTHER_END) > 0) {
                String source = ConversionUtil.hex16Str2String(target);
                String[] arrayNum = null;
                arrayNum = source.split(":[\\s\\S]");//:加一个任意字符
                String numSrc = arrayNum.length > 0 ? arrayNum[1] : "";
                numSrc = numSrc.substring(0, 15);
                return numSrc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static BigDecimal getUdp2CMeterSum(String target) {
        try {
            if (target.indexOf(UDP_T2C) > 0 && StringUtils.isNotEmpty(target) && target.length() > 112) {
                String numStr = target.substring(target.length()-42, target.length()-22);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(UDP_T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[0];
                        String udpStupidBCD = udpStupidBCD(numSrc);
                        BigDecimal finalSumNum = CodeNumUtil.getBigDecimal(udpStupidBCD(numSrc), 2);
                        logger.info("UDP协议（2C）水表累计读数:" + numSrc + " 转义后UDP:" + udpStupidBCD + " 总和读数为:" + finalSumNum);
                        return finalSumNum;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        sb.append(chars[0]);
        sb.append(chars[1]);
        return sb.toString();
    }

    public static BigDecimal getUdp2CMeterAdd(String target) {
        try {
            if (target.indexOf(UDP_T2C) > 0 && StringUtils.isNotEmpty(target) && target.length() > 112) {
                String numStr = target.substring(target.length()-42, target.length()-22);
                if (StringUtils.isNotEmpty(numStr)) {
                    String[] numArray = numStr.split(UDP_T2C);
                    if (numArray.length > 0) {
                        String numSrc = numArray[1];
                        String udpStupidBCD = udpStupidBCD(numSrc);
                        BigDecimal finalAddNum = CodeNumUtil.getBigDecimal(udpStupidBCD(numSrc), 2);
                        logger.info("UDP协议（2C）水表新增读数:" + numSrc + " 转义后UDP:" + udpStupidBCD + " 新增读数为:" + finalAddNum);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0.00);
    }

    /**
     * 检验是否不符合2C符合规则的读数
     * @param target
     * @return
     */
    public static boolean isUdpCheckUn2C(String target) {
        boolean result = false;
        if (target.indexOf(UDP_CUT_68) > 0 && target.indexOf(UDP_OTHER_CHECK) > 0 && target.endsWith(UDP_OTHER_END)) {
            char[] chars = target.toCharArray();
            List<String> strList = new ArrayList<>();
            for (int i = 0; i < chars.length; i++) {
                if (CodeNumUtil.isOdd(i)) {
                    strList.add(String.valueOf(chars[i - 1]) + String.valueOf(chars[i]));
                }
            }
            strList = strList.subList(strList.size() - UDP_OTHER_68_16_LENGTH, strList.size()-2);
            if (strList.contains(UDP_CUT_68) && UDP_CUT_68.equals(strList.get(0))) {
                logger.info("非2C类型水表读数字符列表-{}",strList);
                Object[] objects = strList.toArray();
                Integer sum = 0;
                for (int i = 0; i < objects.length; i++) {
                    sum += Integer.parseInt(String.valueOf(objects[i]), 16);
                }
                Integer finalSum = sum % 256;
                logger.info("非2C类型水表读数-68到检验字节前的累加和求模结果为: {}", finalSum);

                String chkStr = target.substring(target.length() - 4, target.length() - 2);
                int chkNum = Integer.parseInt(chkStr, 16);

                logger.info("检验字节: {}, 值: {}", chkStr, chkNum);
                logger.info("校验结果: {}", finalSum == chkNum);
                result = finalSum == chkNum;
            }
        }
        logger.info("非2C类型水表类型为:{}", result);
        return result;
    }

    public static BigDecimal getUdpUn2CSum(String target) {
        char[] chars = target.toCharArray();
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (CodeNumUtil.isOdd(i)) {
                strList.add(String.valueOf(chars[i - 1]) + String.valueOf(chars[i]));
            }
        }
        strList = strList.subList(strList.size() - UDP_OTHER_68_16_LENGTH, strList.size());
        logger.info("非2C类型水表读数字符列表-{}",strList);
        logger.info("非2C类型水表读数-截取计算水表读数的数组长度为{} , 标准长度为: {}", strList.size(), UDP_OTHER_68_16_LENGTH);
        if (strList.size() == UDP_OTHER_68_16_LENGTH) {
            List<String> strings = strList.subList(14, 18);
            logger.info("截取(15-18)结果: {}", strings.toString());
            String join = StringUtils.join(strings.toArray(), "");
            String s1 = udpStupidBCD(join);
            BigDecimal bigDecimal = CodeNumUtil.getBigDecimal(s1, 2);
            logger.info("读数结果: {}", bigDecimal.toString());
            return bigDecimal;
        }
        return null;
    }


    public static void main(String[] args) {
        //System.out.println("4354524C3A1A32303137303730303030303039333700FEFEFE68103709000000111181091F90000093800000FF2516".length());
        //System.out.println("4354524C3A1A32303137303730303030303039333700FEFEFE68103709000000111181091F90000093800000FF2516".lastIndexOf("16"));
        System.out.println(getUdpMeterAddress("4354524C3A1A32303137303730303030303039333700FEFEFE68103709000000111181091F90000093800000FF2516"));
        System.out.println(getUdpMeterAddress("4354524C3A1A32303137303730303030303039303700FEFEFE6810070900001750018109901F000056330000FFB116"));

        String str_2c = "4354524C3A2B323031373037303030303030383631" +
                "FFDBFBDBDB3BFEFE" +
                "68" +
                "10" +
                "61080000178000" +
                "81" +
                "16" +
                "901F" +
                "00" +
                "00017217" +
                "2C" +
                "00000000" +
                "2C" +
                "00000000000000" +
                "0000" +
                "A0" +
                "16";

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
        //System.out.println("4354524C3A26323031373037303030303030393231".length());
        //
        //System.out.println(ConversionUtil.hex16Str2String("4354524C3A26323031373037303030303030393231"));
        //
        //System.out.println(ConversionUtil.hex16Str2String("FEFEFE"));
        //
        //System.out.println(Integer.parseInt("00020000", 16));
        //
        //System.out.println(Integer.parseInt("00000000", 16));

        System.out.println(getUdpMeterAddress(str_2c));

        System.out.println(getUdp2CMeterSum(str_2c));

        System.out.println(getUdp2CMeterAdd(str_2c));

        System.out.println("-------old----------");

        System.out.println(getUdpMeterAddress(target));

        System.out.println(getUdp2CMeterSum(target));

        System.out.println(getUdp2CMeterAdd(target));

        System.out.println("--------end-------");

        String sip = "4354524C3A1A32303137303730303030303039333700FEFEFE68103709000000111181091F90000093800000FF2516";
        String mp = "4354524C3A3332303137303730303030303039333700FEFEFE6810370900000000008109901F000093800000FF0300FEFEFE68103709000000111181091F90000093800000FF2516";
        System.out.println("4354524C3A1A32303137303730303030303039333700FEFEFE68103709000000111181091F90000093800000FF2516");
        System.out.println("4354524C3A3332303137303730303030303039333700FEFEFE6810370900000000008109901F000093800000FF0300FEFEFE68103709000000111181091F90000093800000FF2516");

        isUdpCheckUn2C(sip);

        getUdpUn2CSum(sip);

        isUdpCheckUn2C(mp);

        getUdpUn2CSum(mp);

        System.out.println(getUdpMeterAddress(sip));
        System.out.println(getUdpMeterAddress(mp));
        System.out.println(getUdpMeterAddress(target));
        System.out.println(getUdpMeterAddress(str_2c));
    }

}
