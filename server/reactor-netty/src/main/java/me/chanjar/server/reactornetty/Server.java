package me.chanjar.server.reactornetty;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.SslProvider;

import java.io.File;

public class Server {

  public static void main(String[] args) {

    DisposableServer disposableServer =
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

    disposableServer.onDispose().block();

  }

}
