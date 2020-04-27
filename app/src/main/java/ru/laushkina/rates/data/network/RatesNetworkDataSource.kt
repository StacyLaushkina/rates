package ru.laushkina.rates.data.network

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.model.Rate

class RatesNetworkDataSource {
    companion object {
        const val ACCESS_KEY = "adfba205d005b83742c46e9160cc6084"
    }

    fun getRates(baseRate: String): Maybe<List<Rate>> {
        return RatesApiFactory.create()
                .getRates(ACCESS_KEY)
                .subscribeOn(Schedulers.io())
                .map { response: RatesResponse -> RatesMapper.mapToRates(response) }
                .observeOn(AndroidSchedulers.mainThread())
    }
}