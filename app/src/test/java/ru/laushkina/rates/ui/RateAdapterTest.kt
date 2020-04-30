package ru.laushkina.rates.ui

import android.widget.EditText
import android.widget.TextView
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RateAdapterTest {
    private val rate1 = RateViewModel(1, 2, "1", 10f, true)
    private val rate2 = RateViewModel(1, 2, "2", 10f, true)
    private val rates = mutableListOf(rate1, rate2)
    private val listener = mock<RateAdapter.ValueChangeListener>()
    private val adapter = spy(RateAdapter(rates, listener, mock()))

    @Test
    fun `set decimal and floating part of value separately when it is not null`() {
        val intEditView = mock<EditText>()
        val floatTextView = mock<TextView>()
        val holder = mock<RateAdapter.ViewHolder> {
            doReturn(intEditView).`when`(mock).valueIntEditView
            doReturn(floatTextView).`when`(mock).valueFractionTextView
        }
        adapter.setValue(holder, 12.23f, true)

        verify(intEditView).setText("12")
        verify(floatTextView).text = "23"
    }

    @Test
    fun `set empty strings to both decimal and floating parts when value is null`() {
        val intEditView = mock<EditText>()
        val floatTextView = mock<TextView>()
        val holder = mock<RateAdapter.ViewHolder> {
            doReturn(intEditView).`when`(mock).valueIntEditView
            doReturn(floatTextView).`when`(mock).valueFractionTextView
        }

        adapter.setValue(holder, null, true)

        verify(intEditView).setText("")
        verify(floatTextView).text = ""
    }

    @Test
    fun `set empty strings to both decimal and floating parts when value is not null, but was told not to be shown`() {
        val intEditView = mock<EditText>()
        val floatTextView = mock<TextView>()
        val holder = mock<RateAdapter.ViewHolder> {
            doReturn(intEditView).`when`(mock).valueIntEditView
            doReturn(floatTextView).`when`(mock).valueFractionTextView
        }

        adapter.setValue(holder, 12.23f, false)

        verify(intEditView).setText("")
        verify(floatTextView).text = ""
    }

    @Test
    fun `change order of elements when click on rate`() {
        doNothing().`when`(adapter).notifyItemMovedToTheTop(any())

        adapter.onRateClicked(1, rate2)

        assertEquals(rate2, adapter.rates[0])
        assertEquals(rate1, adapter.rates[1])
        verify(adapter).notifyItemMovedToTheTop(1)
    }
}