# Server - Tomcat

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

注意Truststore和Keystore的配置。

提供了docker启动脚本方便测试：[start-container.sh](start-container.sh)。

如果有CRL文件，则在`SSLHostConfig`添加属性`certificateRevocationListFile`指向CRL文件所在路径即可。

参考资料：

* https://tomcat.apache.org/tomcat-8.5-doc/ssl-howto.html
* https://tomcat.apache.org/tomcat-8.5-doc/config/http.html#SSL_Support
