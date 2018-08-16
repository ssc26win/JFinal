package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;
import com.shangsc.platform.actual.util.ConversionUtil;
import com.shangsc.platform.actual.util.TcpConvertUtil;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.ActualType;
import com.shangsc.platform.code.TcpData;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualLog;
import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/9/9 0:14
 * @Version 1.0.0
 * @Desc
 */
public class TcpServerHandler extends SimpleChannelHandler {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void log(String msg) {
        logger.info(msg);
    }


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        String clientIP = "";
        try {
            if (ctx.getChannel() != null && ctx.getChannel().getRemoteAddress() != null) {
                clientIP = ctx.getChannel().getRemoteAddress().toString();
            }
            if (StringUtils.isEmpty(clientIP)) {
                InetSocketAddress remoteAddress = (InetSocketAddress) e.getRemoteAddress();
                clientIP = remoteAddress.toString();
            }
            log("TCP received IP:" + clientIP);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        log("TCP received " + buffer.readableBytes() + " bytes [" + buffer.toString() + "]");

        String result = ConversionUtil.bytes2Hex16Str(buffer.array());

        log("TCP ConversionUtil.bytes2HexString 字节数组转16进制字符串 " + result);

        if (StringUtils.isNotEmpty(result) && result.startsWith(ActualType.TCP_PRFIX) && result.endsWith(ActualType.TCP_SUFFIX)) {
            //if (TcpData.login_data_length == result.length()) {
            //    String response = TcpConvertUtil.tcpLoginResp(result, "login");
            //    buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
            //    e.getChannel().write(buffer);
            //}
            if (TcpData.login_data_length == result.length()) {
                //String response = ConversionUtil.tcpLoginResp(result, "login");
                String response = "594E2B49503D3131342E3131352E3133362E323136594E2B504F52543D3130303030594E2B534C454550454E3D30";
                ChannelBuffer bufferTemp = ChannelBuffers.copiedBuffer(ConversionUtil.toStringHex(response).getBytes());
                buffer = bufferTemp;
                e.getChannel().write(buffer);
            }
            if (result.length() == TcpData.upload_data_length_1 || result.length() == TcpData.upload_data_length_2) {
                recordMsg(result);
                recordDB(result, false, clientIP);
                String response = TcpConvertUtil.receiveDataResp(result);
                buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
                e.getChannel().write(buffer);
            }
            if (result.length() > TcpData.upload_data_length_1 || result.length() > TcpData.upload_data_length_2) {
                recordMsg(result);
                recordDB(result, true, clientIP);
                String response = TcpConvertUtil.getTcpMultChkStr(result);
                buffer.setBytes(0, ConversionUtil.hexString2Bytes(response));
                e.getChannel().write(buffer);
            }
            // e.getChannel().close();
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
                FileWriter fw = new FileWriter(path + fileName, true);
                fw.write(ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms) + ":" + msg + "\r\n");
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void recordDB(String result, boolean isMulti, String clientIP) {
        try {
            if (StringUtils.isNotEmpty(result)) {
                String meterAddress = "";
                BigDecimal sumWater = null;
                BigDecimal addWater = new BigDecimal("0.00");
                Integer state = Integer.parseInt(ActualState.NORMAL);
                BigDecimal times = new BigDecimal("1");
                String innerCode = "";
                if (isMulti) {
                    meterAddress = TcpConvertUtil.getTcpMultAddress(result);
                    sumWater = TcpConvertUtil.getTcpMultRecordSumWater(result);
                } else {
                    meterAddress = TcpConvertUtil.getTcpMeterAddress(result);
                    sumWater = TcpConvertUtil.getTcpSumNum(result);
                }
                if (sumWater == null) {
                    logger.info("TCP 未知读数类型: {}", result);
                    return;
                }
                logger.info("[TCP 最终获取表计地址: {} 读数为: {}]", meterAddress, sumWater.toString());
                if (sumWater != null && sumWater.intValue() >= 0 && sumWater.intValue() <= 99999999) { // 表一共就6个位 两位小数 传回来的值不乘倍数之前 大于99999999的 肯定是无效的
                    WaterMeter meter = WaterMeter.me.findByMeterAddress(meterAddress);
                    if (meter != null) {
                        ActualData data = ActualData.me.getLastMeterAddress(meterAddress);
                        if (data != null && data.getSumWater().compareTo(sumWater) > 0) {
                            state = Integer.parseInt(ActualState.EXCEPTION);
                        }
                        innerCode = meter.getInnerCode();
                        times = meter.getTimes();
                        sumWater = times.multiply(sumWater);
                        if (data != null) {
                            addWater = sumWater.subtract(data.getSumWater());
                        } else {
                            addWater = sumWater;
                        }
                        ActualData.me.save(null, innerCode, meterAddress, null, addWater, sumWater, state, "", new Date());
                    } else {
                        log("TCP log not exist meter_address :" + meterAddress);
                    }
                } else {
                    logger.info("TCP 错误数据-sum_water:" + sumWater + "（meterAddress:" + meterAddress + "）");
                }
                // 记录消息来源
                ActualLog.dao.save(null, ActualType.TCP, Integer.parseInt(PropKit.get("config.tcp.port")), clientIP, result, meterAddress, new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}