#!/usr/bin/env bash

# Default variables passed via ENV if unset/blank
if [ -z "$JVM_HEAP_MIN" ]; then L_HEAPMIN="256m"; else L_HEAPMIN="$JVM_HEAP_MIN"; fi
if [ -z "$JVM_HEAP_MAX" ]; then L_HEAPMAX="1024m"; else L_HEAPMAX="$JVM_HEAP_MAX"; fi

# Runtime code coverage collection - if the jacocoagent.jar is present in the
# service directory, and one of the properties files contains
# "jacoco.code.coverage=true" , then collect coverage data.



JAVA_AGENT_JACOCO="";
if [ -f "$PVC_PATH/jars/jacocoagent.jar" ]; then

  SERVICE_NAME=`echo $SERVICE_PATH | sed 's:.*/::'`
  COVERAGE_PATH=$PVC_PATH/logs/$SERVICE_NAME
  PVC_SERVICE_JAR_PATH=$COVERAGE_PATH"/"$SERVICE_NAME.jar

  cp $SERVICE_JAR $PVC_SERVICE_JAR_PATH
   #cp $SERVICE_PATH/jacocoagent.jar $PVC_PATH/jacocoagent.jar
    # Check jacoco.code.coverage setting in any properties file
    # COVERAGE_FILE="$SERVICE_PATH/jacoco/jacoco.exec"
    # JAVA_AGENT_JACOCO="-javaagent:$SERVICE_PATH/jacocoagent.jar=output=tcpserver,address="*",port=6300,append=true";
    # JAVA_AGENT_JACOCO="-javaagent:$SERVICE_PATH/jacocoagent.jar=destfile=$SERVICE_PATH/logs/jacoco.exec";
    # JAVA_AGENT_JACOCO="-javaagent:$PVC_PATH/jars/jacocoagent.jar=destfile=$PVC_PATH/logs/$SERVICE_NAME/jacoco.exec,append=true";
    JAVA_AGENT_JACOCO="-javaagent:$PVC_PATH/jars/jacocoagent.jar=destfile=$COVERAGE_PATH/jacoco.exec,classdumpdir=$COVERAGE_PATH/target,includes="com.example.springreactivedemo.*",append=true";

fi

# Spring profiles - read the configuration files outside of the service code
JAVA_ARGS_PROFILES=""
JAVA_ARGS_CONFIG="-Dspring.config.location=classpath:application.properties"

JAVA_ARGS_SERVER="-server -Dcatalina.base=$APP_HOME"
JAVA_ARGS_APP_HOME="-DAPP_HOME=$APP_HOME"
JAVA_ARGS_MEMORY="-Xms$L_HEAPMIN -Xmx$L_HEAPMAX"
JAVA_ARGS_TOUCH="-DTOUCH_FILE_PATH=$SERVICE_PATH/temp"

JAVA_ARGS="$JAVA_ARGS_SERVER $JAVA_ARGS_APP_HOME $JAVA_ARGS_MEMORY $JAVA_ARGS_PROFILES $JAVA_ARGS_CONFIG $JAVA_ARGS_TOUCH $JAVA_AGENT_JACOCO"

exec java $JAVA_ARGS -jar $SERVICE_JAR