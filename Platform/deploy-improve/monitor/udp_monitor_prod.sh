#!/bin/sh
# function:自动监控tomcat进程，挂了就执行重启操作
# author:ssc
# DEFINE
 
# 获取udp PID
UdpID=$(ps --no-heading -C java -f --width 1000 | grep "actual-udp.jar" | awk '{print $2}')
 
# udp_startup
StartUdp=/usr/local/tomcat7/actual-udp/bin/start.sh

cur_month="`date +%Y-%m`"

UdpMonitorLog=/usr/local/tomcat7/monitor/logs/Prod_UdpMonitor.$cur_month.log
 
Monitor()
{
  echo "[info]开始监控udp...[$(date +'%F %H:%M:%S')]"
  if [ "$UdpID" != "" ];then
    echo "[info] udp 进程ID为:$UdpID."
    echo "[info] udp 运行正常."
  else
    echo "[error]进程不存在!udp自动重启..."
    echo "[info]$StartUdp,请稍候......"
    $StartUdp
  fi
  echo "------------------------------"
}
Monitor>>$UdpMonitorLog
