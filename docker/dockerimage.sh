docker build -t aparnabhure/spring-reactive-demo .
docker push aparnabhure/spring-reactive-demo
docker container run -d -it --name democontainer aparnabhure/spring-reactive-demo:latest
kubectl create -f srd-deployment.yml