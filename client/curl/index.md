# Client - Curl

## Server证书不是权威CA签发，请求时不做TLS服务端认证

```bash
curl --insecure \
  --key certs/client/cert-key.pem \
  --cert certs/client/cert.pem \
  https://localhost:8443/
```

## Server证书不是权威CA签发，请求时做TLS服务端认证

```bash
curl --cacert certs/server/ca.pem \
  --key certs/client/cert-key.pem \
  --cert certs/client/cert.pem \
  https://localhost:8443/
```

## 如果Server证书是权威CA签发

```bash
curl \
  --key certs/client/cert-key.pem \
  --cert certs/client/cert.pem \
  https://localhost:8443/
```

