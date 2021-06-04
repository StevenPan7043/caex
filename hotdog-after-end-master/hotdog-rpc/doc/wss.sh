# jar 执行脚本
# 执行命令: sh wss.sh [start|stop|restart|status]

#!/bin/bash
APP_NAME=zzex-rpc-provider.jar
BASE_PWD=`pwd`

usage() {
 echo "Usage: sh wss.sh [start|stop|restart|status]"
 exit 1
}

#检查程序是否在运行
is_exist(){
 pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
 if [ -z "${pid}" ]; then
 return 1
 else
 return 0
 fi
}

#启动方法
start(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is already running. pid=${pid} ."
 else
 nohup java -jar -Xms258m -Xmx258m -XX:PermSize=512M -XX:MaxPermSize=512m $BASE_PWD/$APP_NAME > $BASE_PWD/operation.file 2>&1 &
 echo "${APP_NAME} start success"
 fi
}

#停止方法
stop(){
 is_exist
 if [ $? -eq "0" ]; then
 sed -i "s/flag=1/flag=0/g" $BASE_PWD/status.config
 kill -9 $pid
 echo "${APP_NAME} stop success"
 else
 echo "${APP_NAME} is not running"
 fi
}
  
#输出运行状态
status(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is running. Pid is ${pid}"
 else
 echo "${APP_NAME} is NOT running."
 fi
}
  
#重启
restart(){
 stop
 start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
 "start")
 start
 ;;
 "stop")
 stop
 ;;
 "status")
 status
 ;;
 "restart")
 restart
 ;;
 *)
 usage
 ;;
esac

