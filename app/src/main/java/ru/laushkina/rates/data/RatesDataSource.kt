package ru.laushkina.rates.data

import io.reactivex.Maybe
import ru.laushkina.rates.model.Rate

interface RatesDataSource {
    fun save(rates: List<Rate>?)

    fun getRates(): Maybe<List<Rate>>

    fun getBaseRate(): Maybe<Rate>
}