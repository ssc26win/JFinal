package com.shangsc.platform.actual.udp;


import com.jfinal.kit.PropKit;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import java.net.InetSocketAddress;

/**
 * @Author ssc
 * @Date 2017/9/8 23:26
 * @Version 1.0.0
 * @Desc
 */
public class UdpServer {

    private ConnectionlessBootstrap udpBootstrap;

    public UdpServer(int port) {
        DatagramChannelFactory channelFactory = new NioDatagramChannelFactory();
        udpBootstrap = new ConnectionlessBootstrap(channelFactory);
        udpBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new UdpEventHandler());
            }
        });
        udpBootstrap.bind(new InetSocketAddress(PropKit.get("config.host"), port));
        System.out.println("udp server started, listening on port:" + port);
    }

    public static void main(String[] args) {
        PropKit.use("config.properties");
        new UdpServer(Integer.parseInt(PropKit.get("config.udp.port")));
    }

}
