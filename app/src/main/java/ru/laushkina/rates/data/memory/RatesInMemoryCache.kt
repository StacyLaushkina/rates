package ru.laushkina.rates.data.memory

import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.model.Rate

class RatesInMemoryCache : RatesDataSource {

    private var rates: List<Rate>? = null

    override fun save(rates: List<Rate>?) {
        this.rates = rates
    }

    override fun getRates(): Maybe<List<Rate>> {
        return Maybe.just(rates)
    }

    override fun getBaseRate(): Maybe<Rate> {
        return Maybe.just(rates).map { rates: List<Rate> -> rates[0] }
    }
}