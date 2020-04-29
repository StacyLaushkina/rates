package ru.laushkina.rates.ui

import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RateAdapterTest {

    @Test
    fun `formatted amount is empty when no need to show it`() {
        assertEquals("", RateAdapter.formatAmount(3f, false))
    }

    @Test
    fun `formatted amount is empty when actual value is null`() {
        assertEquals("", RateAdapter.formatAmount(null, true))
    }

    @Test
    fun `formatted amount is numeric when there is no decimal part`() {
        assertEquals("12", RateAdapter.formatAmount(12f, true))
    }

    @Test
    fun `formatted amount contains two decimal digits when it has it`() {
        assertEquals("12.34", RateAdapter.formatAmount(12.34f, true))
    }

    @Test
    fun `formatted amount contains two decimal digits when it has five`() {
        assertEquals("12.35", RateAdapter.formatAmount(12.34567f, true))
    }

    @Test
    fun `change order of elements when click on rate`() {
        val rate1 = RateViewModel(1, 2, "1", 10f, true)
        val rate2 = RateViewModel(1, 2, "2", 10f, true)
        val rates = mutableListOf(rate1, rate2)
        val listener = mock<RateAdapter.ValueChangeListener>()
        val adapter = spy(RateAdapter(rates, listener, mock()))

        doNothing().`when`(adapter).notifyItemMovedToTheTop(any())

        adapter.onRateClicked(1, rate2)

        assertEquals(rate2, adapter.rates[0])
        assertEquals(rate1, adapter.rates[1])
        verify(adapter).notifyItemMovedToTheTop(1)
    }
}