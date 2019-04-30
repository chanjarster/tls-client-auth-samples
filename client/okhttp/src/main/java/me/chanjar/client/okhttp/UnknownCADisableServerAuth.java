package me.chanjar.client.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class UnknownCADisableServerAuth {

  public static void main(String[] args)
      throws Exception {

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

    Request request = new Request.Builder()
        .url("https://localhost:8443")
        .get()
        .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
    }

  }

}
