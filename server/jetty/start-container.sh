#!/bin/bash

docker run -d \
  --rm \
  --name tls-jetty \
  -v $(pwd)/../../certs:/var/lib/jetty/etc/certs \
  -v $(pwd)/ssl.ini:/var/lib/jetty/start.d/ssl.ini \
  -p 8443:8443 \
  chanjarster/tls-jetty
