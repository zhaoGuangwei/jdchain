#!/bin/bash

#启动Home路径
BOOT_HOME=$(cd `dirname $0`;cd ../; pwd)

#进程启动后PID.log所在路径
PID_LOG=$BOOT_HOME/bin/PID.log

#从启动文件中读取PID
if [ -f "$PID_LOG" ]; then
    # File exist
    echo "Read PID From File:[$PID_LOG] ..."
    PID_LINE=`sed -n '$p' $PID_LOG`
    echo "Last Gateway Boot Info = $PID_LINE ..."
    if [[ $PID_LINE == *GW_BOOT_PID* ]]; then
        LOG_PID=$(echo $PID_LINE | cut -d "=" -f 2 | cut -d "[" -f 2 | cut -d "]" -f 1)
        echo "Last Gateway Boot PID = $LOG_PID ..."
        PID=`ps -ef | grep deployment-gateway | grep $LOG_PID | grep -v grep | awk '{print $2}'`
    fi
#启动文件不存在则直接通过PS进行过滤
else
    PID=`ps -ef | grep $BOOT_HOME/lib/deployment-gateway | grep -v grep | awk '{print $2}'`
fi

#通过Kill命令将进程杀死
if [ -z "$PID" ]; then
    echo "Unable to find gateway PID. stop aborted."
else
    echo "Start to kill PID = $PID ..."
    kill -9 $PID
    echo "Gateway has been stopped ..."
fi