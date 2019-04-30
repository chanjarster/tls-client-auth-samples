package me.chanjar.client.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

public class UnknownCAEnableServerAuth {

  public static void main(String[] args)
      throws Exception {


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

    Request request = new Request.Builder()
        .url("https://localhost:8443")
        .get()
        .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
    }

  }

}
