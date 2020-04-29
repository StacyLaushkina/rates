package ru.laushkina.rates.model

import android.content.Context
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Test
import ru.laushkina.rates.TestUtils
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.network.RatesNetworkDataSource

class RatesServiceTest {
    private val context = mock<Context>()
    private val ratesDBDataSource = mock<RatesDataSource>()
    private val ratesNetworkDataSource = mock<RatesNetworkDataSource>()
    private val ratesMemoryDataSource = mock<RatesDataSource>()

    private val service = RatesService(
            context,
            ratesDBDataSource,
            ratesNetworkDataSource,
            ratesMemoryDataSource
    )

    @Test
    fun `list of rates is reordered and contains correct base rate after base rate change`() {
        val baseRate = Rate(RateShortName.JPY, 15f, true)

        val currentRates = listOf(
                Rate(RateShortName.AFN, 1000f, false),
                Rate(RateShortName.JPY, 15f, false),
                Rate(RateShortName.FJD, 20f, true),
                Rate(RateShortName.MYR, 1.5f, false)
        )
        val changedRates = service.changeBaseRate(baseRate, currentRates)

        assertEquals(4, changedRates.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 15f, true), changedRates[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.AFN, 1000f, false), changedRates[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.FJD, 20f, false), changedRates[2])
        TestUtils.assertRatesEqual(Rate(RateShortName.MYR, 1.5f, false), changedRates[3])
    }

    @Test
    fun `save in both db and memory data sources when call save()`() {
        val currentRates = listOf(Rate(RateShortName.AFN, 1000f, false))
        service.save(currentRates)

        verify(ratesDBDataSource).save(currentRates)
        verify(ratesMemoryDataSource).save(currentRates)
    }

    @Test
    fun `save multiplied values and return rates from cache when call calculateAndSaveRates()`() {
        val currentRates = listOf(
                Rate(RateShortName.AFN, 1000f, false),
                Rate(RateShortName.JPY, 15f, false),
                Rate(RateShortName.FJD, 20f, true),
                Rate(RateShortName.MYR, 1.5f, false)
        )
        service.calculateAndSaveRates(currentRates, 20f, 2f)

        val captor = argumentCaptor<List<Rate>>()
        verify(ratesDBDataSource).save(captor.capture())
        verifyNoMoreInteractions(ratesDBDataSource)

        assertEquals(4, captor.firstValue.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.AFN, 100f, false), captor.firstValue[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 1.5f, false), captor.firstValue[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.FJD, 2f, true), captor.firstValue[2])
        TestUtils.assertRatesEqual(Rate(RateShortName.MYR, 0.15f, false), captor.firstValue[3])

        verify(ratesMemoryDataSource).save(any())
        verify(ratesMemoryDataSource).getRates()
    }

    @Test
    fun `when base rate value increased, other values increased too`() {
        val currentRates = listOf(
                Rate(RateShortName.AFN, 1000f, false),
                Rate(RateShortName.JPY, 15f, false),
                Rate(RateShortName.FJD, 20f, true),
                Rate(RateShortName.MYR, 1.5f, false)
        )
       val result = RatesService.multiply( 20f, 200f, currentRates)

        assertEquals(4, result.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.AFN, 10000f, false), result[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 150f, false), result[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.FJD, 200f, true), result[2])
        TestUtils.assertRatesEqual(Rate(RateShortName.MYR, 15f, false), result[3])
    }

    @Test
    fun `when base rate value decreased, other values decreased too`() {
        val currentRates = listOf(
                Rate(RateShortName.AFN, 1000f, true),
                Rate(RateShortName.JPY, 150f, false),
                Rate(RateShortName.FJD, 20000f, false),
                Rate(RateShortName.MYR, 12345f, false)
        )
        val result = RatesService.multiply( 1000f, 10f, currentRates)

        assertEquals(4, result.size)
        TestUtils.assertRatesEqual(Rate(RateShortName.AFN, 10f, true), result[0])
        TestUtils.assertRatesEqual(Rate(RateShortName.JPY, 1.5f, false), result[1])
        TestUtils.assertRatesEqual(Rate(RateShortName.FJD, 200f, false), result[2])
        TestUtils.assertRatesEqual(Rate(RateShortName.MYR, 123.45f, false), result[3])
    }

    @Test
    fun `return default rate when there is no searched rate in list in `() {
        val rates = listOf(Rate(RateShortName.AFN, 1000f, false))
        TestUtils.assertRatesEqual(
                Rate(RateShortName.USD, 1f, true),
                service.getBaseRateOrDefault(rates, RateShortName.MYR)
        )
    }

    @Test
    fun `return rate when there is searched rate in list in `() {
        val rates = listOf(
                Rate(RateShortName.AFN, 1000f, false),
                Rate(RateShortName.BIF, 1000f, false)
        )
        TestUtils.assertRatesEqual(
                Rate(RateShortName.BIF, 1000f, false),
                service.getBaseRateOrDefault(rates, RateShortName.BIF)
        )
    }
}