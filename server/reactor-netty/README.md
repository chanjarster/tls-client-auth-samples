# Server - Reactor Netty

见[Server.java](src/main/java/me/chanjar/server/reactornetty/Server.java)：

```java
HttpServer
    .create()
    .port(8443)
    .secure(sslContextSpec -> {
      SslContextBuilder sslContextBuilder = SslContextBuilder
          .forServer(
            new File("../../certs/server/cert.pem"),
            new File("../../certs/server/cert-key.p8.pem"),
            "server-key123"
          )
          .trustManager(new File("../../certs/client/ca.pem"))
          .clientAuth(ClientAuth.REQUIRE);

      sslContextSpec.sslContext(sslContextBuilder)
          .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
      ;
    })
    .handle((request, response) ->
        response.sendString(Mono.just("Hello world"))
    )
    .bindNow();
```

