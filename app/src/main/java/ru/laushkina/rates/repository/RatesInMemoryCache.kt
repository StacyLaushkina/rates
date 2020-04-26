package ru.laushkina.rates.repository

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.laushkina.rates.model.Rate

class RatesInMemoryCache {
    private val observable: Subject<List<Rate>> = PublishSubject.create()

    companion object {
        private val lock = Any()

        @Volatile
        private var INSTANCE: RatesInMemoryCache? = null

        // TODO
        fun getInstance(): RatesInMemoryCache {
            if (INSTANCE == null) {
                synchronized(lock) {
                    if (INSTANCE == null) {
                        INSTANCE = RatesInMemoryCache()
                    }
                }
            }
            return INSTANCE!!
        }
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