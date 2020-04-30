package ru.laushkina.rates.ui

import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RateAdapterTest {

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