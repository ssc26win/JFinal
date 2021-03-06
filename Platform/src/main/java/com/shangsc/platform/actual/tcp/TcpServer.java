package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @Author ssc
 * @Date 2017/9/8 23:56
 * @Version 1.0.0
 * @Desc
 */
public class TcpServer {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TcpServer(int port) {
        logger.info("tcp server started, listening on port:" + port);
        ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        sb.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new TcpServerHandler());
            }
        });
        //获取InetSocketAdress对象的InetAddress;
        InetSocketAddress address = new InetSocketAddress(PropKit.get("config.host"), port);

        InetAddress add = address.getAddress();
        sb.bind(new InetSocketAddress(add, port));
    }
}