package ru.laushkina.rates.repository

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.laushkina.rates.model.Rate

class RatesInMemoryCache {
    private val observable: Subject<List<Rate>> = PublishSubject.create()

    companion object {
        val INSTANCE: RatesInMemoryCache by lazy { RatesInMemoryCache() }
    }

    fun getCachedRates(): Subject<List<Rate>> {
        return observable
    }

    fun setCachedRates(rates: List<Rate>?) {
        if (rates != null) {
            observable.onNext(rates)
        }
    }
}