package ru.laushkina.rates.ui

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import junit.framework.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RatesService

class RatesPresenterTest {
    private val service = mock<RatesService>()
    private val workManager = mock<WorkManager>()
    private val view = mock<RatesView>()

    private val presenter = RatesPresenter(service, workManager, view)

    @Test
    fun `all disposables are disposed after onDestroy`() {
        val cacheDisposableSpy = mock<Disposable>()
        val loadDisposableSpy = mock<Disposable>()
        presenter.cacheDisposable = cacheDisposableSpy
        presenter.loadDisposable = loadDisposableSpy

        presenter.onDestroy()

        verify(cacheDisposableSpy).dispose()
        verify(loadDisposableSpy).dispose()
    }


    @Test
    fun `discard periodic update and request immediate rates update when new one is selected `() {
        doReturn(mock<Single<List<Rate>>>()).`when`(service).loadRates(any())

        val rate = RateViewModel(1, 2, "1", 10f, true)
        presenter.onRateSelected(rate)

        verify(workManager).cancelAllWorkByTag("RatesLoadWorker")
        verify(service).loadRates("1")
    }

    @Test
    fun `do not listen to further updates when rate value changes`() {
        val loadDisposableSpy = mock<Disposable>()
        presenter.loadDisposable = loadDisposableSpy

        presenter.beforeRateValueChange()

        verify(loadDisposableSpy).dispose()
        verify(workManager).cancelAllWorkByTag("RatesLoadWorker")

    }

    @Test
    fun `schedule next update after rate value change`() {
        presenter.afterRateValueChange()

        val requestCaptor = ArgumentCaptor.forClass(OneTimeWorkRequest::class.java)
        verify(workManager).enqueue(requestCaptor.capture())
    }

    @Test
    fun `empty all rates when new rate value is empty`() {
        val rate1 = RateViewModel(1, 2, "1", 10f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "")

        val ratesCaptor = ArgumentCaptor.forClass(MutableList::class.java) as ArgumentCaptor<List<RateViewModel>>
        verify(view).showRates(ratesCaptor.capture())

        assertFalse(ratesCaptor.value[0].showAmount())
        assertFalse(ratesCaptor.value[1].showAmount())
    }

    @Test
    fun `empty all rates when new rate value is 0`() {
        val rate1 = RateViewModel(1, 2, "1", 10f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "0")

        val ratesCaptor = ArgumentCaptor.forClass(MutableList::class.java) as ArgumentCaptor<List<RateViewModel>>
        verify(view).showRates(ratesCaptor.capture())

        assertFalse(ratesCaptor.value[0].showAmount())
        assertFalse(ratesCaptor.value[1].showAmount())
    }

    @Test
    fun `increase all rate values when new rate value is bigger then previous`() {
        val rate1 = RateViewModel(1, 2, "1", 1f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "10")

        val ratesCaptor = ArgumentCaptor.forClass(MutableList::class.java) as ArgumentCaptor<List<RateViewModel>>
        verify(view).showRates(ratesCaptor.capture())

        assertTrue(ratesCaptor.value[0].showAmount())
        assertEquals(10f, ratesCaptor.value[0].amount)
        assertTrue(ratesCaptor.value[1].showAmount())
        assertEquals(200f, ratesCaptor.value[1].amount)
    }

    @Test
    fun `decrease all rate values when new rate value is less then previous`() {
        val rate1 = RateViewModel(1, 2, "1", 100f, true)
        val rate2 = RateViewModel(1, 2, "2", 20f, true)

        presenter.currentRates = mutableListOf(rate1, rate2)
        presenter.onRateValueChange(rate1, "10")

        val ratesCaptor = ArgumentCaptor.forClass(MutableList::class.java) as ArgumentCaptor<List<RateViewModel>>
        verify(view).showRates(ratesCaptor.capture())

        assertTrue(ratesCaptor.value[0].showAmount())
        assertEquals(10f, ratesCaptor.value[0].amount)
        assertTrue(ratesCaptor.value[1].showAmount())
        assertEquals(2f, ratesCaptor.value[1].amount)
    }
}