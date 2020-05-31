package ru.laushkina.rates.data.database

import android.content.Context
import io.reactivex.Maybe
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName.Companion.parse

class RatesDBDataSource(context: Context): RatesDataSource {
    private val ratesDatabase: RatesDatabase = RatesDatabase.create(context)

    override fun save(rates: List<Rate>?) {
        ratesDatabase.transactionExecutor.execute {
            ratesDatabase.rateDao().truncate()
            ratesDatabase.rateDao().save(RatesDBMapper.mapToEntity(rates))
        }
    }

    override fun getRates(): Maybe<List<Rate>> {
        return ratesDatabase.rateDao()
                .loadAll()
                .map { entities: List<RateEntity> -> RatesDBMapper.mapToRate(entities) }
    }

    override fun getBaseRate(): Maybe<Rate> {
        return ratesDatabase.rateDao()
                .loadBaseRate()
                .map { entity -> Rate(parse(entity.shortName), entity.amount, entity.isBase) }
    }
}