# TLS客户端认证的例子
本项目提供TLS客户端认证的例子，包括Client和Server。

## 准备工作

本项目已经在`certs`目录下准备好了Client和Server需要的证书、私钥、Keystore和Truststore。如果你想自己动手做一遍可以看[证书准备工作][certs-prepare.md]，你也可以跳过本步骤直接看下面的文章

本项目使用[cfssl](cfssl)生成CA证书及私钥，你也可以使用自己的工具来生成。

## Client例子

### curl

见[client/curl/curl.sh](client/curl/curl.sh)脚本：

```bash
curl --cacert ../../certs/server/ca.pem \
  --key ../../certs/client/cert-key.pem \
  --cert ../../certs/client/cert.pem \
  https://localhost:8443/
```

### HttpClient

见[HttpClientSample.java](client/httpclient/src/main/java/me/chanjar/HttpClientSample.java)代码。

### Reactor Netty

### Netty

## Server例子

### Tomcat

修改`conf/server.xml`文件，添加下列配置：

```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol" maxThreads="150" SSLEnabled="true">
  <SSLHostConfig 
    certificateVerification="required" 
    truststoreFile="/certs/server/server.truststore" 
    truststorePassword="serverts123" 
    truststoreType="PKCS12">
    <Certificate 
      certificateKeyAlias="server" 
      certificateKeystoreFile="/certs/server/server.keystore" 
      certificateKeystoreType="PKCS12" 
      certificateKeystorePassword="server123" 
      type="RSA"/>
  </SSLHostConfig>
</Connector>
```

注意观察Truststore和Keystore的配置。

同时提供了docker启动脚本方便测试：[server/tomcat/start.sh](server/tomcat/start.sh)。

### Jetty

### Nginx

### Haproxy
