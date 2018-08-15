package com.shangsc.platform.code;

/**
 * @Author ssc
 * @Date 2017/9/12 13:54
 * @Version 1.0.0
 * @Desc
 */
public class TcpData {

    public static final String login_data = "68090068C917071600020270017216";

    public static final String upload_data_1 = "68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216";

    public static final String upload_data_2 = "682F0068CA18061500180D7007239178563412046D735B00000000DB2400000000000000000000000000000000000000B629007E16";

    public static final Integer login_data_length = login_data.length();

    public static final Integer upload_data_length_1 = upload_data_1.length();

    public static final Integer upload_data_length_2 = upload_data_2.length();

    public static final String TCP_FN_1 = "04";
    public static final String TCP_FN_2 = "07";

    public static void main(String[] args) {
        System.out.println("68090068C91707160002027001".length());

        System.out.println("68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216".length());

        System.out.println(upload_data_1.substring(26, 26 + 64));
        System.out.println("046D735B00000000DB2400000000000000000000000000000000000000B62900".length());


        System.out.println("68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216".length());
        System.out.println("682D0068CA17091600210D700483FC725BC0BE0D00000000000000000C3800036D735BC8C50D0000000000000000073800D416".length());


    }
}
