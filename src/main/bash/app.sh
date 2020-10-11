#!/bin/bash

BIN=$(cd $(dirname $0);pwd)
BASE_DIR=$(cd ${BIN}/../;pwd)
LIB_DIR=${BASE_DIR}/lib
JAR_FILE=${LIB_DIR}/*.jar
CONF_DIR=${BASE_DIR}/conf
LOG_CONF_FILE=${CONF_DIR}/logback-spring.xml
LOG_DIR=${BASE_DIR}/log
PID_FILE=${BASE_DIR}/pid

USAGE="Usage: app.sh [start|stop|restart|status|help]"

JVM_OPT="-Xms1G -Xmx2G -server -XX:PermSize=96M -XX:MaxPermSize=256M -Xss512K -XX:-OmitStackTraceInFastThrow -Doctopus.conf=${CONF_DIR}"

function start() {
    echo "Starting app ..."

    SPRING_PROFILE="dev"
    if [ ! -n "$1" ]; then
        echo "no setting spring profile, use dev instead!"
    else
        SPRING_PROFILE=$1
        echo "spring profile is ${SPRING_PROFILE}"
    fi

    if [ -f ${PID_FILE} ]; then
        local pid=$(cat ${PID_FILE})
        if is_app_running ${pid}; then
            echo "App has started, pid: ${pid}"
            return
        fi
    fi
    if [ ! -d ${LOG_DIR} ]; then
        mkdir -p ${LOG_DIR}
    fi
    nohup java ${JVM_OPT} -DLOG_DIR=${LOG_DIR} -jar ${JAR_FILE} --spring.profiles.active=${SPRING_PROFILE} --spring.config.location=${CONF_DIR}/ --logging.config=${LOG_CONF_FILE} &>/dev/null 2>&1 &
    echo $! > ${PID_FILE}
    local pid=$(cat ${PID_FILE})
    if is_app_running ${pid}; then
        echo "Start success, pid: ${pid}"
    else
	    echo "Start failed"
    fi
}

function stop() {
    if [ ! -f ${PID_FILE} ];then
	    echo "App has stopped"
	    return
    fi
    
    local pid=$(cat ${PID_FILE})
    echo "Stopping app, pid: ${pid}" 
    attempt=1
    while [ ${attempt} -le 3 ];do
	    if ! is_app_running ${pid}; then
	        rm ${PID_FILE}
            break
        fi
	    echo "Try killing, attempt: ${attempt}"
        kill ${pid}
	    sleep 10
	    attempt=$((${attempt}+1))
    done

    if is_app_running ${pid}; then
	    echo "Stop failed, please retry"
	    return 1
    else
        echo "Stop success"
	    return 0
    fi
}

function restart() {
    if ! stop; then
	    return
    fi
    echo "Restart app"
    start $1
}

function status() {
   if [ ! -f ${PID_FILE} ]; then
       echo "App is not running" 
   else
       local pid=$(cat ${PID_FILE})
       if ! is_app_running ${pid}; then
	       echo "App is not running"
       else
	       echo "App is running, pid: ${pid}"
       fi
    fi
}

function is_app_running() {
    local pid=$1
    app=$(jps | grep "^${pid} ")
    if [ -z "${app}" ]; then
	    return 1
    else
	    return 0
    fi
}

case $1 in
    "start")
	     start $2
	 ;;
    "stop")
         stop
	 ;;
    "restart")
         restart $2
	 ;;
    "status")
         status
	 ;;
    "help")
         echo ${USAGE}
	 ;;

    *)
         echo ${USAGE}
	 ;;
esac
