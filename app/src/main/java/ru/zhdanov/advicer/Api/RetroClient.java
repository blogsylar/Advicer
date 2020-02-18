package ru.zhdanov.advicer.Api;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.zhdanov.advicer.Utils.Constants;

public class RetroClient {
    private static final int CONNECT_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 60;

    private static Retrofit.Builder retrofit;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(createOkHttpClientIgnoreSslErrors())
                    .addConverterFactory(GsonConverterFactory.create());
        }
        retrofit.baseUrl(Constants.baseUrl);
        return retrofit.build();
    }

    private static OkHttpClient createOkHttpClientIgnoreSslErrors() {
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            //sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (java.security.KeyManagementException e) {
            return null;
        } catch (java.security.NoSuchAlgorithmException e) {
            return null;
        }
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return new OkHttpClient.Builder()
                .addInterceptor(createHttpLoggingInterceptor())
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier((s, sslSession) -> true)
                .build();
    }

    private static HttpLoggingInterceptor createHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static Client getApiService() {
        return getRetrofitInstance().create(Client.class);
    }
}
