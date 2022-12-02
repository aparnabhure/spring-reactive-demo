# Commands
<font size="4">
docker images
docker image rm
docker build -t aparnabhure/spring-reactive-demo .
docker push aparnabhure/spring-reactive-demo
docker ps -a
docker stop
docker rm 
docker container run -d -it --name democontainer aparnabhure/spring-reactive-demo:latest


kubectl create -f srd-deployment.yml
kubectl exec -it spring-reactive-demo-pod /bin/bash
kubectl logs -f spring-reactive-demo-pod
kubectl delete pod spring-reactive-demo-pod

kubectl port-forward spring-reactive-demo-pod 6300:6300

# Agent command to dump class files for the package
java -javaagent:/Users/ab732698/Downloads/jacoco-0.8.8/lib/jacocoagent.jar=destfile=/Users/ab732698/Downloads/systemtests/jacoco.exec,append=true,classdumpdir=/Users/ab732698/Downloads/systemtests/target,includes='com.example.springreactivedemo.*' -jar /Users/ab732698/github_ripo/develop/spring-reactive-demo/target/spring-reactive-demo-0.0.1.jar

java -javaagent:/Users/ab732698/Downloads/jacoco-0.8.8/lib/jacocoagent.jar=destfile=/Users/ab732698/Downloads/systemtests/jacoco.exec,append=true,classdumpdir=/Users/ab732698/Downloads/systemtests/target,includes="com.example.springreactivedemo.*",append=true -jar /Users/ab732698/github_ripo/develop/spring-reactive-demo/target/spring-reactive-demo-0.0.1.jar

# Report Generation
mvn org.jacoco:jacoco-maven-plugin:0.8.8:report -Djacoco.dataFile=/Users/ab732698/Downloads/jacoco-2022-11-10_10-43-40.exec

# Report Generation Using jacococli
java -jar /Users/ab732698/Downloads/jacoco-0.8.8/lib/jacococli.jar report /Users/ab732698/Downloads/systemtests/jacoco.exec --classfiles /Users/ab732698/Downloads/systemtests/target --html /Users/ab732698/Downloads/systemtests/report
java -jar /Users/ab732698/Downloads/jacoco-0.8.8/lib/jacococli.jar report /Users/ab732698/Downloads/systemtests/jacoco.exec --classfiles /Users/ab732698/Downloads/systemtests/target --csv /Users/ab732698/Downloads/systemtests/report.csv

# Merge multiple exec
java -jar docker/jacococli.jar merge "/Users/ab732698/Downloads/jacoco-2022-11-11_04-43-20.exec" "/Users/ab732698/Downloads/jacoco-2022-11-11_04-41-12.exec" --destfile /Users/ab732698/Downloads/jacocomerged.exec

# Apply PVC
kubectl apply -f system-tests-pvc.yml

</font>