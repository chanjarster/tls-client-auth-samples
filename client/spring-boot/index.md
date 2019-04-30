# Client - Spring Boot RestTemplate

RestTemplate支持多种Http库（见[文档][doc]）这里举例使用HttpClient的例子，所以代码和[HttpClient](../httpclient/index.md)差不多。

代码见：[RestTemplateConfiguration.java](src/main/java/me/chanjar/client/springboot/RestTemplateConfiguration.java)

## Server证书不是权威CA签发，请求时不做TLS服务端认证

```java
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
```

## Server证书不是权威CA签发，请求时做TLS服务端认证

```bash
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
```

## 如果Server证书是权威CA签发

```java
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
```



[doc]: https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/integration.html#rest-resttemplate-create

