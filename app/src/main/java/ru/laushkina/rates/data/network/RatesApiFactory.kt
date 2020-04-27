package ru.laushkina.rates.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.laushkina.rates.BuildConfig

class RatesApiFactory {
    companion object {
        fun create(): RatesApi {
            val builder = Retrofit.Builder()
                    .baseUrl("http://apilayer.net/api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())

            if (BuildConfig.DEBUG) {
                enableNetworkLogging(builder)
            }
            return builder.build().create(RatesApi::class.java)
        }

        private fun enableNetworkLogging(builder: Retrofit.Builder): Retrofit.Builder? {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()
            builder.client(httpClient)
            return builder
        }
    }
}