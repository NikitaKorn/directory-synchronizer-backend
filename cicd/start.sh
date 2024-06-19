#!/bin/bash

image_tag=1.0.2
image_name="directory-synchronizer-backend"
name="directory-synchronizer-backend"

cd ..
mvn clean -DskipTests package
docker build -t $image_name:$image_tag .
docker tag $image_name:$image_tag localhost:5000/$image_name:$image_tag
docker push localhost:5000/$image_name:$image_tag

kubectl -n $name delete deploy $name
kubectl -n $name delete svc $name

cd cicd
kubectl apply -f Deployment.yaml
sleep 20s
kubectl apply -f Service.yaml