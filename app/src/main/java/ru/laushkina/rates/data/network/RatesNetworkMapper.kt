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

            // Add base currency
            result.add(Rate(RateShortName.parse(response.Source), 1f, true))

            // Add other currencies
            for (entry in response.Rates.entries) {
                result.add(Rate(parseNonBaseRate(entry.key, response.Source), entry.value, false))
            }

            return result
        }

        private fun parseNonBaseRate(rate: String, baseRate: String): RateShortName {
            // All non-base rates start with base rate short name
            return RateShortName.parse(rate.replace(baseRate, ""))
        }
    }
}