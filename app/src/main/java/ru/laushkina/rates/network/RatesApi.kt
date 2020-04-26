package ru.laushkina.rates.network

import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {
    @GET("live")
    fun getRates(@Query("access_key") accessKey: String?): Maybe<RatesResponse>
}