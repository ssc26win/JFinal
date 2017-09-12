package com.shangsc.platform.code;

/**
 * @Author ssc
 * @Date 2017/9/12 13:54
 * @Version 1.0.0
 * @Desc
 */
public class TcpData {

    public static final String login_data = "68090068C917071600020270017216";

    public static final String upload_data = "68090068C91707160002027001067BBF5533332341840000003333B33E64007800D200620006A40B1F0F0158027216";

    public static final Integer login_data_length = login_data.length();

    public static final Integer upload_data_length = upload_data.length();

    public static void main(String[] args) {
        System.out.println("68090068C91707160002027001".length());

        System.out.println("067BBF5533332341840000003333B33E64007800D200620006A40B1F0F015802".length());

        System.out.println(upload_data.substring(26, 26 + 64));
    }

}
