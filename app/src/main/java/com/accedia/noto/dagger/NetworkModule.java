package com.accedia.noto.dagger;

import com.accedia.noto.api.API;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final int TIMEOUT = 30;
    private final String baseURL;

    public NetworkModule(String baseURL) {
        this.baseURL = baseURL;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkhttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new AuthInterceptor(getSharedPreferencesManager()))
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        return client;
    }

    @Provides
    @Singleton
    API providesRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .client(client)
                .build();

        return retrofit.create(API.class);
    }

}
