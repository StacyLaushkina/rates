package ru.laushkina.rates.ui

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import junit.framework.Assert.*
import org.junit.Test
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RatesService

class RatesPresenterTest {
    private val ratesMock = mock<Maybe<List<Rate>>>()
    private val service = mock<RatesService> {
        doReturn(ratesMock).`when`(mock).onRateValueChange(any(), any())
    }
    private val view = mock<RatesView>()

    private val presenter = RatesPresenter(view, service)

    @Test
    fun `empty all rates when new rate value is empty`() {
        val rate1 = RateViewModel(1, 2, "1", 10f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "")

        assertTrue(presenter.emptyAllRateValues)
    }

    @Test
    fun `empty all rates when new rate value is 0`() {
        val rate1 = RateViewModel(1, 2, "1", 10f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "0")

        assertTrue(presenter.emptyAllRateValues)
    }
}