package me.chanjar.client.springboot;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;

@Configuration
public class RestTemplateConfiguration {

  @Bean(name = "unknownCADisableServerAuth")
  public RestTemplate unknownCADisableServerAuth(RestTemplateBuilder restTemplateBuilder) throws Exception {

    SSLContext sslcontext = SSLContexts.custom()
        .loadTrustMaterial(TrustAllStrategy.INSTANCE)
        .loadKeyMaterial(
            new File("../../certs/client/client.keystore"),
            "client123".toCharArray(),
            "client123".toCharArray())
        .build();

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext,
        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    CloseableHttpClient httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslsf)
        .build();

    RestTemplate restTemplate = restTemplateBuilder.build();
    restTemplate.setRequestFactory(
        new HttpComponentsClientHttpRequestFactory(httpclient)
    );
    return restTemplate;

  }

  @Bean(name = "unknownCAEnableServerAuth")
  public RestTemplate unknownCAEnableServerAuth(RestTemplateBuilder restTemplateBuilder) throws Exception {

    SSLContext sslcontext = SSLContexts.custom()
        .loadTrustMaterial(
            new File("../../certs/client/client.truststore"),
            "clientts123".toCharArray()
        )
        .loadKeyMaterial(
            new File("../../certs/client/client.keystore"),
            "client123".toCharArray(),
            "client123".toCharArray())
        .build();

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext,
        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    CloseableHttpClient httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslsf)
        .build();

    RestTemplate restTemplate = restTemplateBuilder.build();
    restTemplate.setRequestFactory(
        new HttpComponentsClientHttpRequestFactory(httpclient)
    );
    return restTemplate;

  }

  @Bean(name = "knownCA")
  public RestTemplate knownCA(RestTemplateBuilder restTemplateBuilder) throws Exception {

    SSLContext sslcontext = SSLContexts.custom()
        .loadKeyMaterial(
            new File("../../certs/client/client.keystore"),
            "client123".toCharArray(),
            "client123".toCharArray())
        .build();

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext,
        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    CloseableHttpClient httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslsf)
        .build();

    RestTemplate restTemplate = restTemplateBuilder.build();
    restTemplate.setRequestFactory(
        new HttpComponentsClientHttpRequestFactory(httpclient)
    );
    return restTemplate;

  }

}
