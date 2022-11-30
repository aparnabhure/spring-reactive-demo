FROM openjdk:11

ENV SERVICE_JAR=spring-reactive-demo-0.0.1.jar
ENV SERVICE_PATH=/usr/local/symantec/spring-reactive-demo
ENV PVC_PATH=/data/systemtests

RUN mkdir -p $SERVICE_PATH
RUN mkdir -p $SERVICE_PATH/temp

COPY target/spring-reactive-demo-0.0.1.jar $SERVICE_PATH/
COPY docker/run.sh $SERVICE_PATH/

# COPY docker/jacocoagent.jar $SERVICE_PATH/
# COPY docker/jacococli.jar $SERVICE_PATH/

RUN chmod -R 755 ${SERVICE_PATH}
RUN chmod -R 777 ${SERVICE_PATH}/temp

WORKDIR ${SERVICE_PATH}

CMD ["./run.sh"]
