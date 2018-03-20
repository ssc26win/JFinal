package com.shangsc.platform.actual;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import com.shangsc.platform.actual.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author ssc
 * @Date 2017/9/8 14:28
 * @Desc 用途：
 */
public class ActualTcpPlugin implements IPlugin {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean start() {
        logger.info("......Tcp plugin start......");
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TcpServer(Integer.parseInt(PropKit.get("config.tcp.port")));
            }
        }).start();
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }
}