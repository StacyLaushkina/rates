package ru.laushkina.rates.network;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.laushkina.rates.BuildConfig;

public class RatesApiFactory {
    public static RatesApi create() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://apilayer.net/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        if (BuildConfig.DEBUG) {
            enableNetworkLogging(builder);
        }

        return builder.build().create(RatesApi.class);
    }

    private static Retrofit.Builder enableNetworkLogging(@NonNull Retrofit.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        builder.client(httpClient.build());

        return builder;
    }
}
