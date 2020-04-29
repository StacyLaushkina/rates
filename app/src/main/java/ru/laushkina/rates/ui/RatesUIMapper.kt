package ru.laushkina.rates.ui

import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import java.util.ArrayList

class RatesUIMapper {
    companion object {
        fun toViewModel(rates: List<Rate>, emptyAllRateValues: Boolean): MutableList<RateViewModel> {
            val result: MutableList<RateViewModel> = ArrayList(rates.size)
            for (rate in rates) {
                val nameImagePair = RateUIInfo.getFullNameAndImage(rate.shortName)
                result.add(RateViewModel(nameImagePair.second, nameImagePair.first, rate.shortName.name, rate.amount, !emptyAllRateValues))
            }

            val baseRate = result[0]
            return result.sortedWith(compareBy(
                    { it.shortName != baseRate.shortName },
                    { it.imageId == RateUIInfo.getUnknownImageId() }
            )).toMutableList()
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