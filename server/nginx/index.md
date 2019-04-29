# Server - Nginx

```nginx
server {
    listen       443 ssl;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

    ssl_certificate /certs/server/cert.pem;
    ssl_certificate_key /certs/server/cert-key.pem;

    ssl_client_certificate /certs/client/ca.pem;
    ssl_verify_client on;
}
```

`ssl_certificate`和`ssl_certificate_key`是服务端证书。

`ssl_client_certificate`和`ssl_verify_client`则是TLS客户端认证的配置。

提供了docker启动脚本方便测试：[start-container.sh](start-container.sh)。

参考资料：[CLIENT-SIDE CERTIFICATE AUTHENTICATION WITH NGINX][link-1]

[link-1]: https://fardog.io/blog/2017/12/30/client-side-certificate-authentication-with-nginx/