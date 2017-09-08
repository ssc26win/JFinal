package com.shangsc.platform.actual.udp;

import com.jfinal.kit.PropKit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpServer {
    private byte[] buf = new byte[1024];
    private DatagramPacket dp = new DatagramPacket(buf, buf.length);
    private DatagramSocket socket;

    private String host;
    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UdpServer(String host, Integer port) {
        try {
            //InetSocketAddress socketAddress = new InetSocketAddress(host, port);
            socket = new DatagramSocket(port);// 创建一接收消息的对象，而不是每次接收消息都创建一个
            System.out.println("***Udp服务器即将启动，等待客户端的连接***");
            while (true) {
                socket.receive(dp);
                String data = new String(dp.getData(), 0, dp.getLength());//利用getData()方法取出内容
                File file = new File(PropKit.get("config.udp.actual.upload.log.path"));
                //File file = new File("D:\\udp\\upload_udp.log");
                if (!file.exists()) { //判断文件不存在就new新文件,写数据
                    try {// java IO流和文件关联
                        file.createNewFile();
                        PrintWriter pw = new PrintWriter(file);
                        pw.print(data + "\t");
                        pw.println();
                        pw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileWriter fw = new FileWriter(PropKit.get("config.udp.actual.upload.log.path"),true);
                        //FileWriter fw = new FileWriter("D:\\udp\\upload_udp.log",true);
                        fw.write(data + "\r\n");
                        fw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //接收到客户端的消息
                String rcvd = Dgram.toString(dp) + ",from address:" + dp.getAddress() + ",port:" + dp.getPort();
                System.out.println("From Client:"+rcvd);
                String echoString = "From Server Echoed:" + rcvd;
                DatagramPacket echo = Dgram.toDatagram(echoString, dp.getAddress(), dp.getPort());
                //将数据包发送给客户端
                socket.send(echo);
            }
        } catch (SocketException e) {
            System.err.println("Can't open socket");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Communication error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PropKit.use("config.properties");
        new UdpServer(PropKit.get("config.host"), Integer.parseInt(PropKit.get("config.udp.port")));
    }
}