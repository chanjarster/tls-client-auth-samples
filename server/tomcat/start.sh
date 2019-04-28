#!/bin/bash

docker run -d \
 --rm \
 --name tls-tomcat \
 -v $(pwd)/../../certs:/certs \
 -v $(pwd)/server.xml:/usr/local/tomcat/conf/server.xml \
 -p 8443:8443 \
 tomcat:8.5-alpine
