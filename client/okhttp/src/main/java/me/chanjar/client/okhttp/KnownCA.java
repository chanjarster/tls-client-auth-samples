package me.chanjar.client.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class KnownCA {

  public static void main(String[] args) throws Exception {

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

    Request request = new Request.Builder()
        .url("https://baidu.com")
        .get()
        .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
    }

  }

}
