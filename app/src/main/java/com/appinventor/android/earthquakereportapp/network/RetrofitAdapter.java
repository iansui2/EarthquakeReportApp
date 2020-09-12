package com.appinventor.android.earthquakereportapp.network;

import com.appinventor.android.earthquakereportapp.variables.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    private static Retrofit retrofit;
    private static Gson gson;
    static ConnectivityInterceptor interceptor = new ConnectivityInterceptor();

    public static synchronized Retrofit getInstance() {

        if (retrofit == null) {
            if (gson == null) {
                gson = new GsonBuilder().setLenient().create();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClientBuilder())
                    .build();
        }

        return retrofit;
    }

    private static OkHttpClient okHttpClientBuilder() {
        // Building of OkHttpClient
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build(); // Builds up OkHttpClient
    }
}
