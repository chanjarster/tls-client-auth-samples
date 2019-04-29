# Server - Haproxy

haproxy.cfg：

```txt
global
  daemon

defaults
  mode     http
  stats    uri        /

frontend http-in
  bind    *:443 ssl crt /certs/server/cert-cert-key.pem ca-file /certs/client/ca.pem verify required
```

`crt`是服务端证书+私钥的合并文件。

`ca-file`和`verify required`则是TLS客户端认证的配置。

因为Haproxy不serve static file，所以使用监控页面来测试`stats uri /`。

提供了docker启动脚本方便测试：[start-container.sh](start-container.sh)。

参考资料：[Client Certificate Authentication with HAProxy][link-1]


[link-1]: http://www.loadbalancer.org/blog/client-certificate-authentication-with-haproxy/