package ru.laushkina.rates.data.memory

import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.model.Rate

class RatesInMemoryCache : RatesDataSource {

    private val observable: Subject<List<Rate>> = PublishSubject.create()

    override fun save(rates: List<Rate>?) {
        if (rates != null) {
            observable.onNext(rates)
        }
    }

    override fun getRates(): Maybe<List<Rate>> {
        return observable.singleElement()
    }

    override fun getBaseRate(): Maybe<Rate> {
        return observable.singleElement().map { rates: List<Rate> -> rates[0] }
    }
}