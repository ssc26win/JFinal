package com.shangsc.platform.actual;

import com.jfinal.plugin.IPlugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author ssc
 * @Date 2017/9/5 1:07
 * @Version 1.0.0
 * @Desc
 */
public class ActualPlugin implements IPlugin {

    static final String MAIN_CONFIG = "main";

    String configName = MAIN_CONFIG;

    Properties props;

    public ActualPlugin(Properties props) {
        this(MAIN_CONFIG, props);
    }

    public ActualPlugin(String configName,Properties props) {
        this.configName = configName;
        this.props = props;
    }

    private static Map<String, Socket> clients = new LinkedHashMap<String, Socket>();
    private static PrintWriter pw = null;
    private static FileWriter fw = null;

    @Override
    public boolean start() {
        int port = 10001;
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                // 获得客户端连接 // 阻塞式方法
                System.out.println("准备阻塞...");
                final Socket client = server.accept();
                System.out.println("阻塞完成...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream is = client.getInputStream();
                            //获取输出流 给客服的写数据
                            OutputStream os = client.getOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            System.out.println("准备read...");
                            while ((len = is.read(buffer)) != -1) {
                                String text=new String(buffer,0,len);
                                System.out.println(text);
                                System.out.println("read完成...");
                                if(text.startsWith("#")){
                                    clients.put(text, client);
                                    os.write("认证成功".getBytes());
                                }else{
                                    os.write("收到了你的请求".getBytes());
                                      //String[] split = text.split(":");
                                      //String key="#"+split[0];
                                      //String content=split[1];
                                    // 关联文件
                                    File file = new File("/usr/local/tomcat7/apache-tomcat-7.0.52/webapps/platform/upload_udp.log");
                                    if(!file.exists()){
                                        // 判断文件不存在就new新文件,写数据
                                        try {
                                            file.createNewFile();
                                            // java IO流和文件关联
                                            pw = new PrintWriter(file);
                                            pw.print(text + "\t");
                                            pw.println();
                                            pw.flush();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                    }else{
                                        // 判断文件存在,就以FileWriter文件追加的方式写文件
                                        try {
                                            fw = new FileWriter("c:\\123.txt",true);
                                            fw.write(text + "\r\n");
                                            fw.flush();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                  //Socket s = clients.get(key);
                                  //if(s!=null){
                                  //    OutputStream output = s.getOutputStream();
                                  //     output.write(content.getBytes());
                                  //}
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

}

