# Client - Reactor Netty

## Server证书不是权威CA签发，请求时不做TLS服务端认证

[UnknownCADisableServerAuth.java](src/main/java/me/chanjar/reactornetty/UnknownCADisableServerAuth.java)

```java
HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {

  SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
  sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
  sslContextBuilder
      .keyManager(
          new File("../../certs/client/cert.pem"),
          new File("../../certs/client/cert-key.p8.pem"),
          "client-key123"
      );

  sslContextSpec.sslContext(sslContextBuilder)
      .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
  ;

});
```

## Server证书不是权威CA签发，请求时做TLS服务端认证

[UnknownCAEnableServerAuth.java](src/main/java/me/chanjar/reactornetty/UnknownCAEnableServerAuth.java)

```bash
HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {

  SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
  sslContextBuilder.trustManager(new File("../../certs/server/ca.pem"));
  sslContextBuilder
      .keyManager(
          new File("../../certs/client/cert.pem"),
          new File("../../certs/client/cert-key.p8.pem"),
          "client-key123"
      );

  sslContextSpec.sslContext(sslContextBuilder)
      .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
  ;

});
```

## 如果Server证书是权威CA签发

[KnownCA.java](src/main/java/me/chanjar/reactornetty/KnownCA.java)

```java
HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {

  SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
  sslContextBuilder
      .keyManager(
          new File("../../certs/client/cert.pem"),
          new File("../../certs/client/cert-key.p8.pem"),
          "client-key123"
      );

  sslContextSpec.sslContext(sslContextBuilder)
      .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
  ;

});
```





