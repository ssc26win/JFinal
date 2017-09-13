package com.shangsc.platform.actual.udp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.actual.util.ConversionUtil;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.ActualType;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualLog;
import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
        log("received " + buffer + " bytes [" + buffer.toString() + "]");

        String result = ConversionUtil.bytes2Hex16Str(buffer.array());

        log("ConversionUtil.bytes2HexString 字节数组转16进制字符串 " + result);

        log("ConversionUtil.bytes2HexString 16进制字符串转字符串 " + ConversionUtil.hex16Str2String(ConversionUtil.bytes2Hex16Str(buffer.array())));

        //e.getChannel().write(e.getMessage());

        //记录消息来源
        ActualLog.dao.save(null, ActualType.UDP, 10001, PropKit.get("config.host"), result, new Date());

        recordMsg(result);

        recordDB(result);
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

    private synchronized void recordDB(String result) {
        try { //TODO 5分钟内返回的同一地址传回来的数，丢弃掉
            if (StringUtils.isNotEmpty(result)) {
                String meterAddress = ConversionUtil.getUdpMeterAddress(result);
                String innerCode = "";
                BigDecimal sumWater = ConversionUtil.getUdpMeterSum(result);
                BigDecimal addWater = ConversionUtil.getUdpMeterAdd(result);
                Integer state = Integer.parseInt(ActualState.NORMAL);
                String voltage = "";
                Date writeTime = new Date();
                if (sumWater.compareTo(new BigDecimal(0.00)) > 0 || addWater.compareTo(new BigDecimal(0.00)) > 0) {
                    ActualData data = ActualData.me.getLastMeterAddress(meterAddress);
                    boolean timesReduce = true;
                    if (data!= null && data.getSumWater().compareTo(sumWater) > 0) {
                        state = Integer.parseInt(ActualState.EXCEPTION);
                    }
                    if (data!= null) {
                        addWater = sumWater.subtract(data.getSumWater());
                        timesReduce = (writeTime.getTime() - data.getWriteTime().getTime()) > 1000*60*5;
                    }
                    BigDecimal times = new BigDecimal("1");
                    WaterMeter meter = WaterMeter.me.findByMeterAddress(meterAddress);
                    if (meter != null) {
                        innerCode = meter.getInnerCode();
                        times = meter.getTimes();
                    }
                    sumWater = times.multiply(sumWater);
                    if (timesReduce) {
                        ActualData.me.save(null, innerCode, meterAddress, null, addWater, sumWater, state, voltage, writeTime);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(Charset.defaultCharset());
    }
}