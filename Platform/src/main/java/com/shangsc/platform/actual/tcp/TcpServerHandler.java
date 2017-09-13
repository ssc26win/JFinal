package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.actual.util.ConversionUtil;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.ActualType;
import com.shangsc.platform.code.TcpData;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualLog;
import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

        String result = ConversionUtil.bytes2Hex16Str(buffer.array());

        System.out.println("ConversionUtil.bytes2HexString 字节数组转16进制字符串 " + result);

        if (StringUtils.isNotEmpty(result)) {
            // 记录消息来源
            ActualLog.dao.save(null, ActualType.TCP, Integer.parseInt(PropKit.get("config.tcp.port")), PropKit.get("config.host"), result, new Date());
            if (TcpData.login_data_length == result.length()) {
                String response = ConversionUtil.tcpLoginResp(result, "login");
                //e.getChannel().write(response);
                buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
                e.getChannel().write(buffer);
            }
            if (result.length() == TcpData.upload_data_length) {
                recordMsg(result);
                recordDB(result, false);
                String response = ConversionUtil.receiveDataResp(result);
                //e.getChannel().write(response);
                buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
                e.getChannel().write(buffer);
            }
            if (result.length() > TcpData.upload_data_length) {
                recordMsg(result);
                recordDB(result, true);
                String response = ConversionUtil.getTcpMultChkStr(result);
                //e.getChannel().write(response);
                buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
                e.getChannel().write(buffer);
            }
        }
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

    private synchronized void recordDB(String result, boolean isMulti) {
        try {
            if (StringUtils.isNotEmpty(result)) {
                if (isMulti) {
                    String meterAddress = ConversionUtil.getTcpMultAddress(result);
                    BigDecimal sumWater = ConversionUtil.getTcpMultRecordSumWater(result);
                    BigDecimal addWater = new BigDecimal("0.00");
                    ActualData data = ActualData.me.getLastMeterAddress(meterAddress);
                    Integer state = Integer.parseInt(ActualState.NORMAL);
                    if (data != null && data.getSumWater().compareTo(sumWater) > 0) {
                        state = Integer.parseInt(ActualState.EXCEPTION);
                    }
                    if (data != null) {
                        addWater = sumWater.subtract(data.getSumWater());
                    }
                    String innerCode = "";
                    BigDecimal times = new BigDecimal("1");
                    WaterMeter meter = WaterMeter.me.findByMeterAddress(meterAddress);
                    if (meter != null) {
                        innerCode = meter.getInnerCode();
                        times = meter.getTimes();
                    }
                    sumWater = times.multiply(sumWater);
                    ActualData.me.save(null, innerCode, meterAddress, null, addWater, sumWater, state, "", new Date());
                } else {
                    String meterAddress = ConversionUtil.getTcpMeterAddress(result);
                    BigDecimal sumWater = ConversionUtil.getTcpSumNum(result);
                    BigDecimal addWater = new BigDecimal("0.00");
                    ActualData data = ActualData.me.getLastMeterAddress(meterAddress);
                    Integer state = Integer.parseInt(ActualState.NORMAL);
                    if (data != null && data.getSumWater().compareTo(sumWater) > 0) {
                        state = Integer.parseInt(ActualState.EXCEPTION);
                    }
                    if (data != null) {
                        addWater = sumWater.subtract(data.getSumWater());
                    }
                    String innerCode = "";
                    BigDecimal times = new BigDecimal("1");
                    WaterMeter meter = WaterMeter.me.findByMeterAddress(meterAddress);
                    if (meter != null) {
                        innerCode = meter.getInnerCode();
                        times = meter.getTimes();
                    }
                    sumWater = times.multiply(sumWater);
                    ActualData.me.save(null, innerCode, meterAddress, null, addWater, sumWater, state, "", new Date());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(ConversionUtil.hex16Str2String("067BBF55"));
    }
}