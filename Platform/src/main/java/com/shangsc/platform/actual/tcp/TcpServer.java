package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author ssc
 * @Date 2017/9/8 14:33
 * @Desc 用途：
 */
public class TcpServer {

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

    public TcpServer(String host, Integer port) {
        try {
            //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;
            //记录客户端的数量
            int count=0;
            System.out.println("***Tcp服务器即将启动，等待客户端的连接***");
            //循环监听等待客户端的连接
            while(true){
                //调用accept()方法开始监听，等待客户端的连接
                socket = serverSocket.accept();
                readData(socket);
                count++;//统计客户端的数量
                System.out.println("客户端的数量："+count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP：" + address.getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void readData(Socket socket) {
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        OutputStream os=null;
        PrintWriter pw=null;
        try {
            //获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String info = null;
            File file = new File(PropKit.get("config.tcp.actual.upload.log.path"));
            //File file = new File("D:\\tcp\\upload_tcp.log");
            if (!file.exists()) { //判断文件不存在就new新文件,写数据
                try {// java IO流和文件关联
                    file.createNewFile();
                    pw = new PrintWriter(file);
                    while((info=br.readLine())!=null) {//循环读取客户端的信息
                        System.out.println("服务器-客户端消息：" + info);
                        pw.print(info + "\t");
                        pw.println();
                        pw.flush();
                    }
                    socket.shutdownInput();//关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    FileWriter fw = new FileWriter(PropKit.get("config.tcp.actual.upload.log.path"), true);
                    //FileWriter fw = new FileWriter("D:\\tcp\\upload_tcp.log",true);
                    StringBuffer sb = new StringBuffer("");
                    while((info=br.readLine())!=null) {//循环读取客户端的信息
                        System.out.println("服务器-客户端消息：" + info);
                        sb.append(info);
                        fw.write(info + "\r\n");
                    }
                    System.out.println("客户端消息：" + sb.toString());
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关闭资源
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void analyze(String msg) {

    }
}
