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