#!/bin/sh
# function:自动监控tomcat进程，挂了就执行重启操作
# author:ssc
# DEFINE
 
# 获取tomcat PPID
TomcatID=$(ps -ef |grep tomcat |grep -w 'apache-tomcat-7.0.52'|grep -v 'grep'|awk '{print $2}')
 
# tomcat_startup
StartTomcat=/usr/local/tomcat7/apache-tomcat-7.0.52/bin/startup.sh
 
 
#TomcatCache=/usr/local/tomcat7/apache-tomcat-7.0.52/
 
# 定义要监控的页面地址
WebUrl=http://47.94.196.59/login
 
# 日志输出
GetPageInfo=/usr/local/tomcat7/monitor/logs/Prod_GetPageInfo.log

TomcatMonitorLog=/usr/local/tomcat7/monitor/logs/Prod_TomcatMonitor.log
 
Monitor()
{
  echo "[info]开始监控tomcat...[$(date +'%F %H:%M:%S')]"
  if [ "$TomcatID" != "" ];then
    echo "[info]tomcat进程ID为:$TomcatID."
    # 获取返回状态码

    TomcatServiceCode=$(curl -s -o $GetPageInfo -m 10 --connect-timeout 10 $WebUrl -w %{http_code})

    if [ $TomcatServiceCode -eq 200 ];then
        echo "[info]返回码为$TomcatServiceCode,tomcat运行正常,页面正常."
    else
        echo "[error]访问出错，状态码为$TomcatServiceCode,错误日志已输出到$GetPageInfo"
        echo "[error]开始重启tomcat"
        kill -9 $TomcatID  # 杀掉原tomcat进程
        sleep 3
        #rm -rf $TomcatCache # 清理tomcat缓存
        $StartTomcat
	echo "[info]tomcat启动成功."
    fi
  else
    echo "[error]进程不存在!tomcat自动重启..."
    echo "[info]$StartTomcat,请稍候......"
    #rm -rf $TomcatCache
    $StartTomcat
  fi
  echo "------------------------------"
}
Monitor>>$TomcatMonitorLog
