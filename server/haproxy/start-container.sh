#!/bin/bash

docker run -d \
  --rm \
  --name tls-haproxy \
  -v $(pwd)/../../certs:/certs \
  -v $(pwd)/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg \
  -p 8443:443 \
  haproxy:1.9