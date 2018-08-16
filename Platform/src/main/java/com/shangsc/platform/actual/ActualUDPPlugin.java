package com.shangsc.platform.actual;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import com.shangsc.platform.actual.udp.UdpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Author ssc
 * @Date 2017/9/5 1:07
 * @Version 1.0.0
 * @Desc
 */
public class ActualUdpPlugin implements IPlugin, Serializable {

    private static final long serialVersionUID = 4922524075267548041L;
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean start() {
        logger.info("......Udp plugin start......");
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

