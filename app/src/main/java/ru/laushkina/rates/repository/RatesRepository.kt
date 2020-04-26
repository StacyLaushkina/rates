package ru.laushkina.rates.repository

import io.reactivex.Maybe
import ru.laushkina.rates.model.Rate

interface RatesRepository {
    fun save(rates: List<Rate>?)

    fun getRates(): Maybe<List<Rate>>

    fun getBaseRate(): Maybe<Rate>
}