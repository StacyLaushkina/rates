package ru.laushkina.rates.repository

import android.content.Context
import io.reactivex.Maybe
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName.Companion.parse
import java.util.*

//TODO datasource
class RatesDbRepository(context: Context): RatesRepository {
    private val ratesDatabase: RatesDatabase = RatesDatabase.getInstance(context)

    override fun save(rates: List<Rate>?) {
        ratesDatabase.transactionExecutor.execute {
            ratesDatabase.rateDao().truncate()
            ratesDatabase.rateDao().save(mapToEntity(rates))
        }
    }

    override fun getRates(): Maybe<List<Rate>> {
        return ratesDatabase.rateDao()
                .loadAll()
                .map { entities: List<RateEntity> -> mapToRate(entities) }
    }

    override fun getBaseRate(): Maybe<Rate> {
        return ratesDatabase.rateDao()
                .loadBaseRate()
                .map { entity -> Rate(parse(entity.shortName), entity.amount, entity.isBase) }
    }

    private fun mapToEntity(rates: List<Rate>?): List<RateEntity>? {
        if (rates == null) return null

        val result: MutableList<RateEntity> = ArrayList(rates.size)
        for (rate in rates) {
            result.add(RateEntity(rate))
        }
        return result
    }

    private fun mapToRate(entities: List<RateEntity>): List<Rate> {
        val result: MutableList<Rate> = ArrayList(entities.size)
        for (entity in entities) {
            result.add(Rate(parse(entity.shortName), entity.amount, entity.isBase))
        }
        return result
    }
}