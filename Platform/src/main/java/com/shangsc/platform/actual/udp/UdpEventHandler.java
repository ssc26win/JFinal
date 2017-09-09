package com.shangsc.platform.actual.udp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.actual.TypeConversionUtil;
import com.shangsc.platform.util.ToolDateTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/9/8 23:27
 * @Version 1.0.0
 * @Desc
 */
public class UdpEventHandler extends SimpleChannelUpstreamHandler {

    private void log(Object msg) {
        System.out.println(msg);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        log("messageReceived");
        ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
        log("received " + buffer.readableBytes() + " bytes [" + buffer.toString() + "]");
        log("received " + buffer + " bytes [" + buffer.toString() + "]");
        log("------start-------");

        log("TypeConversionUtil.bytes2HexString 字节数组转16进制字符串 " + TypeConversionUtil.bytes2Hex16Str(buffer.array()));

        log("TypeConversionUtil.bytes2HexString 16进制字符串转字符串 " + TypeConversionUtil.hex16Str2String(TypeConversionUtil.bytes2Hex16Str(buffer.array())));

        byte[] ayy = buffer.array();
        System.out.println("单个字节数组打印：");
        for (int i = 0; i <ayy.length; i++) {

            System.out.println(ayy[i]);
        }
        log("------end-------");

        System.out.println("receive:" + buffer.toString(Charset.defaultCharset()));

        System.out.println("receive:" + buffer.toString(Charset.forName("gbk")));

        System.out.println("receive:" + buffer.toString(Charset.forName("gb2312")));

        //e.getChannel().write(e.getMessage());
        recordMsg(buffer.toString(Charset.defaultCharset()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        log("exceptionCaught");
    }

    private synchronized void recordMsg(String msg) {
        String fileName = PropKit.get("config.udp.log");
        String path = PropKit.get("config.udp.actual.upload.log.path");
        File file = new File(path + fileName);
        if (!file.exists()) {
            try {
                File pathFile = new File(path);
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
                file.createNewFile();
                if (file.exists()) {
                    PrintWriter pw = new PrintWriter(file);
                    pw.print(ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms) + ":" + msg + "\r\n");
                    pw.println();
                    pw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter fw = new FileWriter(path + fileName,true);
                fw.write(ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms) + ":" + msg + "\r\n");
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("byte array 将指定byte数组以16进制的形式打印到控制台: " + hex.toUpperCase() );
        }
    }

    public static void main(String[] args) {
        System.out.println(Charset.defaultCharset());
    }
}