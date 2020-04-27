package ru.laushkina.rates.ui

import android.util.Pair
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import java.util.ArrayList

class RatesUIMapper {
    companion object {
        fun toViewModel(rates: List<Rate>, emptyAllRateValues: Boolean): MutableList<RateViewModel> {
            val result: MutableList<RateViewModel> = ArrayList(rates.size)
            var nameImagePair: Pair<Int, Int>
            for (rate in rates) {
                nameImagePair = RateFullName.getFullName(rate.shortName)
                result.add(RateViewModel(nameImagePair.second, nameImagePair.first, rate.shortName.name, rate.amount, !emptyAllRateValues))
            }
            return result
        }

        fun fromViewModel(rates: List<RateViewModel>): MutableList<Rate> {
            val result: MutableList<Rate> = ArrayList(rates.size)
            for (i in rates.indices) {
                result.add(Rate(RateShortName.parse(rates[i].shortName), rates[i].amount, i == 0))
            }
            return result
        }
    }
}