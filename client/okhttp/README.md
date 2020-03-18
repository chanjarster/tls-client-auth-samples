# Client - OkHttp

## Server证书不是权威CA签发，请求时不做TLS服务端认证

[UnknownCADisableServerAuth.java](src/main/java/me/chanjar/client/okhttp/UnknownCADisableServerAuth.java)

```java
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
KeyStore ks = KeyStore.getInstance("PKCS12");
char[] kspass = "client123".toCharArray();
ks.load(new FileInputStream("../../certs/client/client.keystore"), kspass);
kmf.init(ks, kspass);

final X509TrustManager insecureTm = new X509TrustManager() {
  @Override
  public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
  }
  @Override
  public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
  }
  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
};
final TrustManager[] trustManagers = new TrustManager[] { insecureTm };

SSLContext sslContext = SSLContext.getInstance("TLS");

sslContext.init(kmf.getKeyManagers(), trustManagers, null);
SSLSocketFactory socketFactory = sslContext.getSocketFactory();

final OkHttpClient client = new OkHttpClient.Builder()
    .sslSocketFactory(socketFactory)
    .build();

```

## Server证书不是权威CA签发，请求时做TLS服务端认证

[UnknownCAEnableServerAuth.java](src/main/java/me/chanjar/client/okhttp/UnknownCAEnableServerAuth.java)

```bash
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
KeyStore ks = KeyStore.getInstance("PKCS12");
char[] kspass = "client123".toCharArray();
ks.load(new FileInputStream("../../certs/client/client.keystore"), kspass);
kmf.init(ks, kspass);

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
KeyStore ts = KeyStore.getInstance("PKCS12");
char[] tspass = "clientts123".toCharArray();
ts.load(new FileInputStream("../../certs/client/client.truststore"), tspass);
tmf.init(ts);
final TrustManager[] trustManagers = tmf.getTrustManagers();

SSLContext sslContext = SSLContext.getInstance("TLS");

sslContext.init(kmf.getKeyManagers(), trustManagers, null);
SSLSocketFactory socketFactory = sslContext.getSocketFactory();

final OkHttpClient client = new OkHttpClient.Builder()
    .sslSocketFactory(socketFactory, (X509TrustManager) trustManagers[0])
    .build();
```

## 如果Server证书是权威CA签发

[KnownCA.java](src/main/java/me/chanjar/client/okhttp/KnownCA.java)

```java
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
KeyStore ks = KeyStore.getInstance("PKCS12");
char[] kspass = "client123".toCharArray();
ks.load(new FileInputStream("../../certs/client/client.keystore"), kspass);
kmf.init(ks, kspass);

SSLContext sslContext = SSLContext.getInstance("TLS");

sslContext.init(kmf.getKeyManagers(), null, null);
SSLSocketFactory socketFactory = sslContext.getSocketFactory();

final OkHttpClient client = new OkHttpClient.Builder()
    .sslSocketFactory(socketFactory)
    .build();
```





