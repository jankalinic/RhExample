

# Redhat Example api-server /Java

## Parts

 - java11
 - docker
 - k8s
 - maven
 - vert.x
 - junit5

## Content

 


**Makefile contains 3 targets**

    

 - build: which executes mvn package (api-server)

    

 - docker: builds "api-server-image" docker image from this folder

    

 - push: tags "api-server-image:latest ->   docker.io/jankalinic/api-server:latest"
             and pushes to docker.io/jankalinic/api-server:latest

**api-server + pom.xml**

 - runs http-server using vert.x - listening on localhost:8088

 - when reached with HttpMethod GET, responds with text "hello from
   server ID: < uniqueID > with HOSTNAME: < currentHostname >"

**Junit5 tests**

 - testing mainly responses for methods and ports used

**Dockerfile**

 - based on openjdk 11
 - copies built api-server

***Bonus**

 - kubernetes yamls, containing settings for deploying 5 pods with api-server
 - exposing port 8088 with NodePort exposing 8088 for outside communications with cluster (tested on minikube)



> standalone api-server run:

 `java -jar target/api-server-1.0-jar-with-dependencies.jar run redhatExample.VertxServer`"
