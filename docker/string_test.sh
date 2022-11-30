SYSTEM_TESTS_COVERAGE_LOCATION=/Users/ab732698/Downloads

for i in $(ls -d $SYSTEM_TESTS_COVERAGE_LOCATION/jacoco-0.8.8/*/); do
  i=`echo $i | sed 's/.$//'`
  REPORT_COMMAND="java -jar $SYSTEM_TESTS_COVERAGE_LOCATION/lib/jacococli.jar report $i/jacoco.exec --classfiles $i/target --html $i/report";
  echo "report command "$REPORT_COMMAND;
done

#SERVICE_JAR=spring-reactive-demo-0.0.1.jar
#SERVICE_PATH=/usr/local/symantec/spring-reactive-demo
#PVC_PATH=/data/systemtests
#
#SERVICE_NAME=`echo $SERVICE_PATH | sed 's:.*/::'`
#echo $SERVICE_PATH | sed 's:.*/::'
#echo $SERVICE_PATH
#echo $SERVICE_NAME
#
#SYSTEM_TESTS_COVERAGE_ENABLED=true
#
#JAVA_AGENT_JACOCO=""
#if [ "$SYSTEM_TESTS_COVERAGE_ENABLED" = "true" ]; then
#    echo "ST is enabled"
#    # copy service Jar to coverage location
#    # cp $SERVICE_JAR $SERVICE_COVERAGE_LOCATION
#    # Prepare jacoco agent command argument
#    JAVA_AGENT_JACOCO="-javaagent:$SYSTEM_TESTS_COVERAGE_LOCATION/jars/jacocoagent.jar=destfile=$SERVICE_COVERAGE_LOCATION/jacoco.exec,classdumpdir=$SERVICE_COVERAGE_LOCATION/target,includes="com.symantec.saep.notificationaggregator.*",append=true";
#  fi
#
#SYSTEM_TESTS_COVERAGE_LOCATION="/Users/ab732698/Downloads/jacoco-0.8.8"
#
#if [ "$SYSTEM_TESTS_COVERAGE_ENABLED" = "true" ]; then
#  AGENT_JAR_LOCATION=$SYSTEM_TESTS_COVERAGE_LOCATION"/lib/jacocoagent.jar"
#  AGENT_CLI_JAR_LOCATION=$SYSTEM_TESTS_COVERAGE_LOCATION"/lib/jacococli.jar"
#  if [[ ! -f "$AGENT_JAR_LOCATION" ]]; then
#    # copy jacoco Jars to coverage location
#    echo "agent jar not exists"
#    cp $SERVICE_PATH/jacocoagent.jar $AGENT_JAR_LOCATION
#  fi
#  if [[ ! -f "$AGENT_CLI_JAR_LOCATION" ]]; then
#    echo "cli jar not exists"
#      # copy jacoco Jars to coverage location
#      cp $SERVICE_PATH/jacococli.jar $AGENT_CLI_JAR_LOCATION
#  fi
#fi
#
##if [ -z "$SYSTEM_TESTS_COVERAGE_ENABLED" ]; then
##  # Variable not declared, do-nothing
##  echo "Variable not declared"
##  JAVA_AGENT_JACOCO="";
##else
##  echo "Variable found"
##  if [ "$SYSTEM_TESTS_COVERAGE_ENABLED" = "true" ]; then
##    echo "ST is enabled"
##    # copy service Jar to coverage location
##    # cp $SERVICE_JAR $SERVICE_COVERAGE_LOCATION
##    # Prepare jacoco agent command argument
##    JAVA_AGENT_JACOCO="-javaagent:$SYSTEM_TESTS_COVERAGE_LOCATION/jars/jacocoagent.jar=destfile=$SERVICE_COVERAGE_LOCATION/jacoco.exec,classdumpdir=$SERVICE_COVERAGE_LOCATION/target,includes="com.symantec.saep.notificationaggregator.*",append=true";
##  fi
##fi
#
#echo $JAVA_AGENT_JACOCO
#
##JAVA_AGENT_JACOCO="";
##COVERAGE_PATH=$PVC_PATH/logs/$SERVICE_NAME
##SERVICE_BASE_PACKAGE=\"com.example.springreactivedemo.*\"
##JAVA_AGENT_JACOCO="-javaagent:$PVC_PATH/jars/jacocoagent.jar=destfile=$COVERAGE_PATH/jacoco.exec,classdumpdir=$COVERAGE_PATH/target,includes=$SERVICE_BASE_PACKAGE,append=true";
##echo $JAVA_AGENT_JACOCO
##
##PVC_SERVICE_JAR_PATH=$COVERAGE_PATH"/"$SERVICE_NAME.jar
##echo $PVC_SERVICE_JAR_PATH
#
#
#
##java -jar /Users/ab732698/Downloads/jacoco-0.8.8/lib/jacococli.jar report /Users/ab732698/Downloads/systemtests/jacoco.exec --classfiles /Users/ab732698/Downloads/systemtests/target --html /Users/ab732698/Downloads/systemtests/report
#
