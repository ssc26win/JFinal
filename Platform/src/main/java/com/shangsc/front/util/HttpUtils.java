package com.shangsc.front.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String METHOD_POST = "POST";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_TEXT = "application/x-www-form-urlencoded";

    private static final int DEFAULT_CONNECT_TIMEOUT = 5 * 1000;

    private static final int DEFAULT_READ_TIMEOUT = 5 * 1000;

    public static byte[] doGet(String str_url, int connectTimeout, int readTimeout) {
        HttpURLConnection conn = null;
        InputStream input = null;
        try {

            URL url = new URL(str_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            if (connectTimeout <= 0) {
                conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            } else {
                conn.setConnectTimeout(connectTimeout);
            }
            if (readTimeout <= 0) {
                conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
            } else {
                conn.setReadTimeout(readTimeout);
            }
            input = conn.getInputStream();
            return parseFromInputStream(input);
        } catch (Exception e) {
            logger.error("HttpUtils Get Error", e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] doGet(String str_url) {
        //默认的超时时间connect 5s, read 5s
        return doGet(str_url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public static String doGetStr(String str_url, String encoding) {
        //默认的超时时间connect 5s, read 5s
        byte[] res_bytes = doGet(str_url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
        try {
            return new String(res_bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(res_bytes);
        }
    }

    public static String doGetStr(String str_url, String encoding, int connectTimeout, int readTimeout) {
        byte[] res_bytes = doGet(str_url, connectTimeout, readTimeout);
        try {
            return new String(res_bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(res_bytes);
        }
    }


    public static String doPostMapStr(String str_url, String params
            , int conn_timeout, int read_timeout, String encoding) {
        byte[] res_bytes = doPost(str_url, params, conn_timeout, read_timeout);
        try {
            return new String(res_bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(res_bytes);
        }
    }

    public static String doPostStr(String str_url, Map<String, String> params
            , int conn_timeout, int read_timeout, String encoding) {
        byte[] res_bytes = doPost(str_url, params, conn_timeout, read_timeout);
        try {
            return new String(res_bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(res_bytes);
        }
    }

    public static byte[] doPost(String str_url, Map<String, String> paramsMap, int conn_timeout, int read_timeout) {
        StringBuilder sb = new StringBuilder();
        if (paramsMap != null && paramsMap.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
        }
        return doPost(str_url, sb.toString(), conn_timeout, read_timeout);
    }

    public static byte[] doPost(String str_url, String params, int conn_timeout, int read_timeout) {
        return doPost(str_url, params, conn_timeout, read_timeout, new HashMap<String, String>());
    }

    public static byte[] doPost(String str_url, String params, int conn_timeout, int read_timeout, Map<String, String> headerMap) {
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            URL url = new URL(str_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // Set the post method. Default is GET
            connection.setRequestMethod(METHOD_POST);
            // Post cannot use caches
            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            connection.setConnectTimeout(conn_timeout);

            connection.setReadTimeout(read_timeout);
            // This method takes effects to
            // every instances of this class.
            // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
            // connection.setFollowRedirects(true);

            // This methods only
            // takes effacts to this
            // instance.
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(false);

            // Set the content type to urlencoded,
            // because we will write
            // some URL-encoded content to the
            // connection. Settings above must be set before connect!
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
            // 进行编码
            connection.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_TEXT);
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.setRequestProperty("connection", "close");

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.connect();


            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.write(params.getBytes("UTF-8"));
//            out.writeBytes(params);
            out.flush();
            out.close();
            input = connection.getInputStream();
            return parseFromInputStream(input);
        } catch (Exception e) {
            logger.error("HttpUtils Post Error", e);
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] parseFromInputStream(InputStream input) throws IOException {
        byte[] by_result = new byte[input.available()];
        input.read(by_result);
        input.close();
        return by_result;
    }

}