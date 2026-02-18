package com.admission.counsellor.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    /*
     *  ⚠️  BASE URL — update this before running!
     *
     *  Android Emulator (Spring Boot on localhost):
     *      http://10.0.2.2:8080/
     *
     *  Real device on same WiFi as your PC:
     *      http://192.168.1.XX:8080/   ← find your PC IP with ipconfig / ifconfig
     */
    private static final String BASE_URL = "192.168.90.117:8080/";    //one change***

    private static RetrofitClient instance;
    private final ApiService apiService;

    private RetrofitClient() {
        // Logs every request/response in Logcat (tag: OkHttp)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    // Thread-safe singleton
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}