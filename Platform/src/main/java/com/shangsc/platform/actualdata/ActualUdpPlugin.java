package com.shangsc.platform.actualdata;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import com.shangsc.platform.actualdata.udp.UdpServer;

/**
 * @Author ssc
 * @Date 2017/9/5 1:07
 * @Version 1.0.0
 * @Desc
 */
public class ActualUdpPlugin implements IPlugin {

    @Override
    public boolean start() {
        System.out.println("......Udp plugin start......");
        new Thread(new Runnable() {
            @Override
            public void run() {
                new UdpServer(Integer.parseInt(PropKit.get("config.udp.port")));
            }
        }).start();
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

}

