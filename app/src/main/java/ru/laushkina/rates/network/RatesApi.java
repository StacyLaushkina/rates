package ru.laushkina.rates.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RatesApi {
    @GET("live")
    Single<RatesResponse> getRates(@Query("access_key") String accessKey);
}
