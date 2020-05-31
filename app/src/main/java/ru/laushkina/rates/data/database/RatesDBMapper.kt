package ru.laushkina.rates.data.database

import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import java.util.ArrayList

class RatesDBMapper {
    companion object {
        fun mapToEntity(rates: List<Rate>?): List<RateEntity>? {
            if (rates == null) return null

            val result: MutableList<RateEntity> = ArrayList(rates.size)
            for (rate in rates) {
                result.add(RateEntity(rate))
            }
            return result
        }

        fun mapToRate(entities: List<RateEntity>): List<Rate> {
            val result: MutableList<Rate> = ArrayList(entities.size)
            for (entity in entities) {
                result.add(Rate(RateShortName.parse(entity.shortName), entity.amount, entity.isBase))
            }
            return result
        }
    }
}