package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.util.ToolDateTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/9/9 0:14
 * @Version 1.0.0
 * @Desc
 */
public class TcpServerHandler extends SimpleChannelHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        System.out.println("received " + buffer.readableBytes() + " bytes [" + buffer.toString() + "]");
        System.out.println("receive:" + buffer.toString(Charset.defaultCharset()));
        //e.getChannel().write(e.getMessage());
        recordMsg(buffer.toString(Charset.defaultCharset()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    private synchronized void recordMsg(String msg) {
        String fileName = PropKit.get("config.tcp.log");
        String path = PropKit.get("config.tcp.actual.upload.log.path");
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
}