#!/bin/bash

image_tag=1.0.2
image_name="directory-synchronizer-backend"
name="directory-synchronizer-backend"

#Mvn package
cd ..
mvn clean -DskipTests package
#Delete old images
docker rmi $(docker images | grep '$image_name')
#Docker build and push
docker build -t $image_name:$image_tag .
docker tag $image_name:$image_tag localhost:5000/$image_name:$image_tag
docker push localhost:5000/$image_name:$image_tag

#Delete old deploy and svc
kubectl -n $name delete deploy $name
kubectl -n $name delete svc $name

#Apply new deploy
cd cicd
kubectl apply -f Deployment.yaml
wait 2s

pod_name=$(kubectl -n $name get pod -l app=$name -o jsonpath="{.items[0].metadata.name}")

#Wait until running and apply service
kubectl -n $name wait --for=condition=Running pod/$pod_name
kubectl apply -f Service.yaml

#Log pods status
kubectl -n $name get pods