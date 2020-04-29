package ru.laushkina.rates.ui

import android.util.Pair
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
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

    @Test
    fun `to view model`() {
        // TODO spies does not work here
//        val rate1 = Rate(RateShortName.JPY, 13.4f, true)
//        val rate2 = Rate(RateShortName.USD, 0.5f, false)
//        val rate3 = Rate(RateShortName.EGP, 0.05f, false)
//        val rates = listOf(rate1, rate2, rate3)
//
//        val mapper = spy(RatesUIMapper()) {
//            doReturn(Pair(1, 1)).whenever(mock).getFullNameAndImagePair(rate1)
//            doReturn(Pair(2, 2).whenever(mock).getFullNameAndImagePair(rate2)
//            doReturn(Pair(3, 3).whenever(mock).getFullNameAndImagePair(rate3)
//        }
//
//        val viewModel = mapper.toViewModel(rates, false)
//        assertEquals(3, viewModel.size)
    }
}