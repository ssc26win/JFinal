#!/bin/sh
# function:自动监控tomcat进程，挂了就执行重启操作
# author:ssc
# DEFINE
 
# 获取tcp PID
TcpID=$(ps --no-heading -C java -f --width 1000 | grep "actual-tcp.jar" | awk '{print $2}')
 
# tcp_startup
StartTcp=/usr/local/tomcat7/actual-tcp/bin/start.sh

cur_month="`date +%Y-%m`"

TcpMonitorLog=/usr/local/tomcat7/monitor/logs/Prod_TcpMonitor.$cur_month.log
 
Monitor()
{
  echo "[info]开始监控tcp...[$(date +'%F %H:%M:%S')]"
  if [ "$TcpID" != "" ];then
    echo "[info] tcp 进程ID为:$TcpID."
    echo "[info] tcp 运行正常."
  else
    echo "[error]进程不存在!tcp自动重启..."
    echo "[info]$StartTcp,请稍候......"
    $StartTcp
  fi
  echo "------------------------------"
}
Monitor>>$TcpMonitorLog
