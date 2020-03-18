# Server - Jetty

为了测试方便制作了一个Jetty Docker Image：

```dockerfile
FROM jetty:9

RUN java -jar "$JETTY_HOME/start.jar" --add-to-startd=ssl,https
COPY ssl.ini /var/lib/jetty/start.d/ssl.ini
```

ssl.ini配置文件内容：

```ini
--module=ssl

jetty.sslContext.keyStorePath=etc/certs/server/server.keystore
jetty.sslContext.keyStorePassword=server123
jetty.sslContext.keyStoreType=PKCS12
jetty.sslContext.keyManagerPassword=server123

jetty.sslContext.trustStorePath=etc/certs/server/server.truststore
jetty.sslContext.trustStorePassword=serverts123
jetty.sslContext.trustStoreType=PKCS12

jetty.sslContext.needClientAuth=true
```

注意Truststore和Keystore的配置。

提供了docker启动脚本方便测试：[start-container.sh](start-container.sh)。

参考文档：

* https://www.eclipse.org/jetty/documentation/9.4.x/configuring-ssl.html
* https://www.eclipse.org/jetty/documentation/9.4.x/jetty-ssl-distribution.html