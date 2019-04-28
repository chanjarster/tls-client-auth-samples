#!/bin/bash

curl --cacert ../../certs/server/ca.pem \
  --key ../../certs/client/cert-key.pem \
  --cert ../../certs/client/cert.pem \
  https://localhost:8443/
