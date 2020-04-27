package ru.laushkina.rates.model

import java.util.ArrayList

class RatesCalculator {
    companion object {
        private fun multiply(original: MutableList<Rate>, value: Float): MutableList<Rate> {
            val updatedValues: MutableList<Rate> = ArrayList()
            for (rate in original) {
                updatedValues.add(
                        Rate(rate.shortName, rate.amount * value, rate.isBase)
                )
            }
            return updatedValues
        }
    }
}