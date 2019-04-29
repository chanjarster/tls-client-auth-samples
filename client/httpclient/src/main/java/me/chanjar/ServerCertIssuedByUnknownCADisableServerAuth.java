package me.chanjar;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class ServerCertIssuedByUnknownCADisableServerAuth {

  public static void main(String[] args)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException,
      KeyManagementException {

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

    HttpGet httpGet = new HttpGet("https://localhost:8443");
    CloseableHttpResponse response = httpclient.execute(httpGet);
    try {
      System.out.println(response.getStatusLine());
      HttpEntity entity = response.getEntity();
      System.out.println(EntityUtils.toString(entity));
    } finally {
      response.close();
    }

  }

}
