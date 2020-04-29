package ru.laushkina.rates.data.network

import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import java.util.*

class RatesNetworkMapper {
    companion object {
        fun mapToRates(response: RatesResponse?): List<Rate> {
            if (response?.Rates == null) {
                return listOf()
            }

            val result: MutableList<Rate> = ArrayList<Rate>()
            for (entry in response.Rates.entries) {
                val shortName = parseRateShortName(entry.key, response.Source)
                result.add(Rate(shortName, entry.value, shortName.name == response.Source))
            }
            return result
        }

        private fun parseRateShortName(rate: String, baseRate: String): RateShortName {
            // All non-base rates start with base rate short name
            return RateShortName.parse(rate.replaceFirst(baseRate, ""))
        }
    }
}