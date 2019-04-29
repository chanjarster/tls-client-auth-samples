#!/bin/bash

docker run -d \
  --rm \
  --name tls-nginx \
  -v $(pwd)/../../certs:/certs \
  -v $(pwd)/default.conf:/etc/nginx/conf.d/default.conf \
  -p 8443:443 \
  nginx:1.15