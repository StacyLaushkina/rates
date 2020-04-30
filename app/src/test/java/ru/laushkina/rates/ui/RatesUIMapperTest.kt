package ru.laushkina.rates.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.laushkina.rates.TestUtils
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName

@RunWith(JUnit4::class)
class RatesUIMapperTest {
    @Test
    fun `from view model`() {
        val rate1 = RateViewModel(1, 1, RateShortName.JPY.name, 13.4f, true)
        val rate2 = RateViewModel(2, 2, RateShortName.USD.name, 0.5f, true)
        val rate3 = RateViewModel(3, 3, RateShortName.EGP.name, 0.05f, true)
        val viewModels = listOf(rate1, rate2, rate3)

        val rates = RatesUIMapper.fromViewModel(viewModels)
        assertEquals(3, rates.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 13.4f, true), rates[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.USD, 0.5f, false), rates[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.EGP, 0.05f, false), rates[2])
    }
}