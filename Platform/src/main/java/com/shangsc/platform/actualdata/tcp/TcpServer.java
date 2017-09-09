package com.shangsc.platform.actualdata.tcp;

import com.jfinal.kit.PropKit;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
/**
 * @Author ssc
 * @Date 2017/9/8 23:56
 * @Version 1.0.0
 * @Desc
 */
public class TcpServer {

    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("tcp server started, listening on port:" + port);
        ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        sb.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new TcpServerHandler());
            }
        });
        sb.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) {
        PropKit.use("config.properties");
        new TcpServer(Integer.parseInt(PropKit.get("config.tcp.port"))).run();
    }
}