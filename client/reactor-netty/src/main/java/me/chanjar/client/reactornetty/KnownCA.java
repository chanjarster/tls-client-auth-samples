package me.chanjar.client.reactornetty;

import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

import java.io.File;

public class KnownCA {

  public static void main(String[] args) {

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

    System.out.println(
        httpClient.get().uri("https://baidu.com").responseContent().aggregate().asString().block()
    );

  }

}
