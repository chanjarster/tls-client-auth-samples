# Client - HttpClient

## Server证书不是权威CA签发，请求时不做TLS服务端认证

[UnknownCADisableServerAuth.java](src/main/java/me/chanjar/client/hc/UnknownCADisableServerAuth.java)

```java
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
```

## Server证书不是权威CA签发，请求时做TLS服务端认证

[UnknownCAEnableServerAuth.java](src/main/java/me/chanjar/client/hc/UnknownCAEnableServerAuth.java)

```bash
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
```

## 如果Server证书是权威CA签发

[KnownCA.java](src/main/java/me/chanjar/client/hc/KnownCA.java)

```java
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
```





