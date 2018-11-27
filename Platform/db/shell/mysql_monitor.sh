#!/bin/sh
# function:自动监控mysql进程，挂了就执行重启操作
# author:ssc
# DEFINE
 
# 获取mysql PPID
MysqlID=$(ps -ef | grep "mysqld" | grep -v "mysqld_safe"| grep -v "grep" | awk '{print $2}')

# mysql_startup
StartMysql=$(/etc/init.d/mysqld start)
 
# 日志输出

MysqlMonitorLog=/usr/local/tomcat7/monitor/logs/MonitorMysql.log
 
Monitor()
{
  echo "[info]开始监控mysql...[$(date +'%F %H:%M:%S')]"
  if [ "$MysqlID" != "" ];then
    echo "[info]mysql运行正常，进程ID为:$MysqlID."
  else
    echo "[error]进程不存在!mysql自动重启..."
    echo "[info]$StartMysql,请稍候......"

    #重启mysql
    $StartMysql

    if [ $? -eq 0 ]; then
      echo "Start mysql succeed"
      #重启所有服务

      #重启udp 
      /usr/local/tomcat7/actual-udp/bin/stop.sh && /usr/local/tomcat7/actual-udp/bin/start.sh
      #重启tcp
      /usr/local/tomcat7/actual-tcp/bin/stop.sh && /usr/local/tomcat7/actual-tcp/bin/start.sh
      #重启platform
      /usr/local/tomcat7/apache-tomcat-7.0.52/bin/shutdown.sh && /usr/local/tomcat7/apache-tomcat-7.0.52/bin/startup.sh
      #重启测试platform
      /usr/local/tomcat7/tomcat-test/bin/shutdown.sh && /usr/local/tomcat7/tomcat-test/bin/startup.sh

    else
      echo "Start mysql failed"
    fi
  fi
  echo "------------------------------"
}
Monitor>>$MysqlMonitorLog
