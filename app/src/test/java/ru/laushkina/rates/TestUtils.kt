package ru.laushkina.rates

import org.junit.Assert
import ru.laushkina.rates.model.Rate

class TestUtils {
    companion object {
        fun assertRatesEqual(rate1: Rate, rate2: Rate) {
            Assert.assertEquals(rate1.shortName, rate2.shortName)
            Assert.assertEquals(rate1.amount, rate2.amount)
            Assert.assertEquals(rate1.isBase, rate2.isBase)
        }
    }
}