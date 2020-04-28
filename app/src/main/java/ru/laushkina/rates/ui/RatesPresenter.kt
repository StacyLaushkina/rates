package ru.laushkina.rates.ui

import androidx.annotation.VisibleForTesting
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.data.network.RatesLoadWorker
import ru.laushkina.rates.model.RateShortName
import java.util.concurrent.TimeUnit

class RatesPresenter(private val ratesService: RatesService,
                     private val workManager: WorkManager,
                     private val ratesView: RatesView) {

    @VisibleForTesting
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var baseRate: RateViewModel? = null

    @VisibleForTesting
    var currentRates: MutableList<RateViewModel> = arrayListOf()

    private var emptyAllRateValues = false

    companion object {
        private const val TAG = "RatesPresenter"
        private val UPDATE_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(1)
    }

    fun onCreate() {
        compositeDisposable.add(ratesService.initialize().subscribe(
                { rates: List<Rate> -> this.onRatesLoaded(rates) },
                ratesView::showError
        ))
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onRateSelected(selectedRate: RateViewModel) {
        compositeDisposable.add(
                ratesService.onBaseRateChange(
                Rate(RateShortName.parse(selectedRate.shortName), selectedRate.amount, true),
                RatesUIMapper.fromViewModel(currentRates)
        )
                .subscribe(
                { rates: List<Rate> -> this.onRatesLoaded(rates) },
                ratesView::showError
        ))
    }

    fun beforeRateValueChange() {
    }

    fun afterRateValueChange() {
    }

    fun onRateValueChange(rate: RateViewModel, value: String) {
        var floatValue = value.toFloatOrNull()
        if (floatValue == null || floatValue == 0f) {
            emptyAllRateValues = true
            floatValue = 1f
        } else {
            emptyAllRateValues = false
        }

        compositeDisposable.add(ratesService
                .onRateValueChange(rate.amount, floatValue)
                .subscribe(
                        { rates: List<Rate> -> this.onRatesUpdated(rates) },
                        ratesView::showError
                )
        )
    }

    private fun onRatesLoaded(rates: List<Rate>) {
        onRatesUpdated(rates)
        scheduleNextUpdate()
    }

    private fun onRatesUpdated(rates: List<Rate>) {
        if (rates.isEmpty()) return

        currentRates = RatesUIMapper.toViewModel(rates, emptyAllRateValues)
        val baseRate = currentRates[0]
        this.baseRate = baseRate
        ratesView.showRates(currentRates)
    }

    private fun scheduleNextUpdate() {
        val rate = Data.Builder()
                .putString(RatesLoadWorker.BASE_RATE_SHORT_NAME, baseRate?.shortName)
                .putString(RatesLoadWorker.BASE_RATE_AMOUNT, baseRate?.amount.toString())
                .build()
        val mRequest = OneTimeWorkRequest.Builder(RatesLoadWorker::class.java)
                .setInitialDelay(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .addTag(RatesLoadWorker.TAG)
                .setInputData(rate)
                .build()
        workManager.enqueue(mRequest)
    }
}