package com.shangsc.platform.actual.tcp;

import com.jfinal.kit.PropKit;

import java.io.*;
import java.net.Socket;
/**
 * @Author ssc
 * @Date 2017/9/8 14:31
 * @Desc 用途：
 * 服务器线程处理类
 */
public class TcpServerThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;

    public TcpServerThread(Socket socket) {
        this.socket = socket;
    }

    //线程执行的操作，响应客户端的请求
    public void run() {
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
                    while((info=br.readLine())!=null) {//循环读取客户端的信息
                        System.out.println("服务器-客户端消息：" + info);
                        fw.write(info + "\r\n");
                    }
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
}