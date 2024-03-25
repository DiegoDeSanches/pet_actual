package com.khandras.elastic.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;

@Service
public class ElasticAdapter {
    private static final String SERVER_URL = "https://localhost:9200";

    private static final SSLContext sslContext;
    private static final RestClient restClient;

    static {
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{ UnsafeX509ExtendedTrustManager.INSTANCE }, null);

            restClient = RestClient
                    .builder(new HttpHost("localhost", 9200, "https"))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder.setSSLContext(sslContext)
                                    .setSSLHostnameVerifier((host, session) -> true))
                    .setDefaultHeaders(new Header[]{
                            new BasicHeader("Authorization", "Basic " + prepareAuth())
                    })
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Retryable(maxAttempts = 20, backoff = @Backoff(10L))
    public void sendMessage(String message, String author, String year, String bookName) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        var request = new Request(HttpMethod.POST.name(), SERVER_URL + "/book/_doc");
        request.addParameter("pretty", "true");
        request.setJsonEntity(ElasticMessage.prepareMessage(message, author, year, bookName));

        restClient.performRequest(request);

    }

    private static String prepareAuth() {
        String plainCreds = "elastic:changeme";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);

        return new String(base64CredsBytes);
    }

    @Getter
    static class ElasticMessage {
        private static final ObjectMapper mapper = new ObjectMapper();
        private final String message;
        private final String author;
        private final String year;
        private final String bookName;
        private final String timestamp;

        private ElasticMessage(String message, String author, String year, String bookName) {
            this.author = author;
            this.bookName = bookName;
            this.message = message;
            this.year = year;
            this.timestamp = String.valueOf(Instant.now().toEpochMilli());
        }

        @SneakyThrows
        public static String prepareMessage(String message, String author, String year, String bookName) {
            var elasticMessage = new ElasticMessage(message, author, year, bookName);
            return mapper.writeValueAsString(elasticMessage);
        }
    }
}

final class UnsafeX509ExtendedTrustManager extends X509ExtendedTrustManager {
    public static final X509ExtendedTrustManager INSTANCE = new UnsafeX509ExtendedTrustManager();
    private static final X509Certificate[] EMPTY_CERTIFICATES = new X509Certificate[0];

    private UnsafeX509ExtendedTrustManager() {}

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

    }

    public static X509ExtendedTrustManager getInstance() {
        return INSTANCE;
    }


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return EMPTY_CERTIFICATES;
    }

}
