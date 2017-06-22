package com.annguyen.android.parti.api;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String DIRECTIONS_API_KEY = "AIzaSyCpLASZdMpd8HiXA58oAutyFoOq3HJVYAg";
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";

    private Retrofit retrofit;

    public RetrofitClient() {

        //create an interceptor to inject api key into url
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //get original request
                Request originalRequest = chain.request();
                //get original url
                HttpUrl originalUrl = originalRequest.url();

                //inject api key into url
                HttpUrl newUrl = originalUrl.newBuilder()
                        .addQueryParameter("key", DIRECTIONS_API_KEY)
                        .build();

                //build new request with new url
                Request newRequest = originalRequest.newBuilder()
                        .url(newUrl)
                        .build();

                //return interceptor with api-key-injected request
                return chain.proceed(newRequest);
            }
        };

        //create a OkHttpClient with the interceptor
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //build a retrofit client with the httpClient
        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public DirectionsService getDirectionsService() {
        return retrofit.create(DirectionsService.class);
    }
}
