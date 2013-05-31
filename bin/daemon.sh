#!/bin/sh

EXEC="$PROJECT_HOME/bin/jsvc"
CLASS_PATH="$PROJECT_HOME/build/output/lib/*":"$PROJECT_HOME/build/libs/trade.jar"
CLASS="com.zygon.trade.Service"
PID="/tmp/daemon.pid"
LOG_OUT="/tmp/daemon.out"
LOG_ERR="/tmp/daemon.err"

do_exec()
{
  echo $CLASS_PATH
  echo $JAVA_HOME
  echo $CLASS
  $EXEC -home $JAVA_HOME -classpath $CLASS_PATH -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
}

case "$1" in
    start)
        do_exec
            ;;
    stop)
        do_exec "-stop"
            ;;
    restart)
        if [ -f "$PID" ]; then
            do_exec "-stop"
            do_exec
        else
            echo "service not running, will do nothing"
            exit 1
        fi
            ;;
    *)
            echo "usage: daemon {start|stop|restart}" >&2
            exit 3
            ;;
esac
