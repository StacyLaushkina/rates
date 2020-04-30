package ru.laushkina.rates.data.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.laushkina.rates.TestUtils
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName

class RatesNetworkMapperTest {
    @Test
    fun `list of rates is null when rates in response are null`() {
        assertEquals(0, RatesNetworkMapper.mapToRates(null).size)
    }

    @Test
    fun `list of non empty rates mapped from non empty response is correct`() {
        val response = RatesResponse().apply {
            Source = "USD"
            Rates = mapOf(Pair("USDEUR", 0.4f), Pair("USDKRW", 1222.5f), Pair("USDJPY", 104.16f), Pair("USDUSD", 1f))
        }

        val rates = RatesNetworkMapper.mapToRates(response)
        assertEquals(4, rates.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.EUR, 0.4f, false), rates[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.KRW, 1222.5f, false), rates[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 104.16f, false), rates[2])
        TestUtils.assertRatesEqual(Rate(RateShortName.USD, 1f, true), rates[3])
    }
}