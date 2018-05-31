package com.uniben.attsys.api;


import android.util.Log;

import com.uniben.attsys.utils.Constants;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.regex.Pattern.matches;
import static org.apache.http.conn.ssl.AbstractVerifier.getDNSSubjectAlts;

/**
 * Created by Cyberman on 05/30/2018.
 */

public class ServiceGenerator {
    private static final String TAG = "ServiceGenerator";


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();


    public static <S> S createService(
            Class<S> serviceClass) {


        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.i(TAG, "verify: " + hostname);
/*
                Certificate[] certs;
                try {
                    certs = session.getPeerCertificates();
                } catch (SSLException e) {
                    return false;
                }
                X509Certificate x509 = (X509Certificate) certs[0];
                // We can be case-insensitive when comparing the host we used to
                // establish the socket to the hostname in the certificate.
                String hostName = hostname.trim().toLowerCase(Locale.ENGLISH);
                // Verify the first CN provided. Other CNs are ignored. Firefox, wget,
                // curl, and Sun Java work this way.
                String firstCn = getFirstCn(x509);
                if (matches(hostName, firstCn)) {
                    return true;
                }
                for (String cn : getDNSSubjectAlts(x509)) {
                    if (matches(hostName, cn)) {
                        return true;
                    }
                }*/
                if (hostname.contains("teenoh.webfactional.com")) {
                    return true;

                }

                return false;
            }
        });

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);

    }


    private static String getFirstCn(X509Certificate cert) {
        String subjectPrincipal = cert.getSubjectX500Principal().toString();
        for (String token : subjectPrincipal.split(",")) {
            int x = token.indexOf("CN=");
            if (x >= 0) {
                return token.substring(x + 3);
            }
        }
        return null;
    }


}
