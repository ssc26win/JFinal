package com.shangsc.platform.actual.udp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.actual.util.ConversionUtil;
import com.shangsc.platform.actual.util.UdpConvertUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/9/8 23:27
 * @Version 1.0.0
 * @Desc
 */
public class UdpEventHandler extends SimpleChannelUpstreamHandler {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void log(String msg) {
        logger.info(msg);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String clientIP = "";
        try {
            if (ctx.getChannel() != null && ctx.getChannel().getRemoteAddress() != null) {
                clientIP = ctx.getChannel().getRemoteAddress().toString();
            }
            if (StringUtils.isEmpty(clientIP)) {
                InetSocketAddress remoteAddress = (InetSocketAddress) e.getRemoteAddress();
                clientIP = remoteAddress.toString();
            }
            log("UDP received IP:" + clientIP);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        log("UDP messageReceived");
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        log("UDP received " + buffer + " bytes [" + buffer.toString() + "]");

        String result = ConversionUtil.bytes2Hex16Str(buffer.array());

        log("UDP ConversionUtil.bytes2HexString 字节数组转16进制字符串 " + result);

        log("UDP ConversionUtil.bytes2HexString 16进制字符串转字符串 " + ConversionUtil.hex16Str2String(ConversionUtil.bytes2Hex16Str(buffer.array())));

        String chkStr = ConversionUtil.hex16Str2String(ConversionUtil.bytes2Hex16Str(buffer.array()));
        //e.getChannel().write(e.getMessage());
        if (StringUtils.isNotEmpty(chkStr) && chkStr.indexOf(ActualType.UDP_PRFIX) >= 0) {

            //recordMsg(result);

            recordDB(result, clientIP);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        log("UDP exceptionCaught");
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
                FileWriter fw = new FileWriter(path + fileName, true);
                fw.write(ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms) + ":" + msg + "\r\n");
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void recordDB(String result, String clientIP) {
        try {
            if (StringUtils.isNotEmpty(result)) {
                String meterAddress = UdpConvertUtil.getUdpMeterAddress(result);
                if (StringUtils.isEmpty(meterAddress)) {
                    log("转换表计地址失败！");
                    return;
                }
                BigDecimal sumWater = null;
                BigDecimal addWater = new BigDecimal("0.00");

                if (result.indexOf(UdpConvertUtil.UDP_T2C) > 0) {
                    sumWater = UdpConvertUtil.getUdp2CMeterSum(result);
                }
                if (sumWater == null && UdpConvertUtil.isUdpCheckUn2C(result)) {
                    sumWater = UdpConvertUtil.getUdpUn2CSum(result);
                }
                if (sumWater == null) {
                    logger.info("UDP 未知读数类型: {}", result);
                    return;
                }

                logger.info("[UDP 最终获取表计地址: {} 读数为: {}]", meterAddress, sumWater.toString());

                String innerCode = "";

                Integer state = Integer.parseInt(ActualState.NORMAL);
                String voltage = "";
                Date writeTime = new Date();
                BigDecimal times = new BigDecimal("1");
                if (sumWater != null && sumWater.intValue() >= 0 && sumWater.intValue() <= 99999999) {
                    WaterMeter meter = WaterMeter.me.findByMeterAddress(meterAddress);
                    if (meter != null) {
                        ActualData data = ActualData.me.getLastMeterAddress(meterAddress);
                        boolean timesReduce = true;
                        innerCode = meter.getInnerCode();
                        times = meter.getTimes();
                        sumWater = times.multiply(sumWater);
                        if (data != null && data.getSumWater().compareTo(sumWater) > 0) {
                            state = Integer.parseInt(ActualState.EXCEPTION);
                        } else {
                            addWater = sumWater;
                        }
                        if (data != null) {
                            addWater = sumWater.subtract(data.getSumWater());
                            timesReduce = (writeTime.getTime() - data.getWriteTime().getTime()) > 1000 * 60 * 1;
                        }
                        if (timesReduce) {
                            ActualData.me.save(null, innerCode, meterAddress, null, addWater, sumWater, state, voltage, writeTime);
                        }
                    } else {
                        log("UDP log not exist meter_address :" + meterAddress);
                    }
                } else {
                    logger.info("UDP 错误数据-sum_water:" + sumWater + "（meterAddress:" + meterAddress + "）");
                }
                //记录消息来源
                ActualLog.dao.save(null, ActualType.UDP, Integer.parseInt(PropKit.get("config.udp.port")), clientIP, result, meterAddress, writeTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String test = "4354524C3A26323031373037303030303030393231FEFEFE6810210900001700028116901F00000200002C000000002C0000000000000000005B16";
        System.out.println(test.length());
        System.out.println(UdpConvertUtil.getUdpMeterAddress(test));
        System.out.println(UdpConvertUtil.getUdp2CMeterSum(test));
        System.out.println(UdpConvertUtil.getUdp2CMeterAdd(test));
        BigDecimal sumWater = new BigDecimal("011");
        if (sumWater != null && sumWater.intValue() >= 0 && sumWater.intValue() <= 99999999) {
            System.out.println("ok");
        }
    }
}