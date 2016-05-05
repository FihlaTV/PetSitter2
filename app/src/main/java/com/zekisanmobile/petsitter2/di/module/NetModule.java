package com.zekisanmobile.petsitter2.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zekisanmobile.petsitter2.api.ApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class NetModule {

    String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient () {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return client;
    }

    @Provides
    @Singleton
    ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    ApiService providesApiService(ObjectMapper mapper, OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .baseUrl(baseUrl)
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
