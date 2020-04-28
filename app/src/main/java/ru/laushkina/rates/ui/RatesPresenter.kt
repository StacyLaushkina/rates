package ru.laushkina.rates.ui

import androidx.annotation.VisibleForTesting
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.data.network.RatesLoadWorker
import ru.laushkina.rates.model.RateShortName
import java.util.concurrent.TimeUnit

class RatesPresenter(private val ratesService: RatesService, private val ratesView: RatesView) {
    @VisibleForTesting
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    @VisibleForTesting
    var currentRates: MutableList<RateViewModel> = arrayListOf()

    private var emptyAllRateValues = false

    fun onCreate() {
        compositeDisposable.add(ratesService.initialize().subscribe(
                { rates: List<Rate> -> this.onRatesUpdated(rates) },
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
                { rates: List<Rate> -> this.onRatesUpdated(rates) },
                ratesView::showError
        ))
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

    private fun onRatesUpdated(rates: List<Rate>) {
        if (rates.isEmpty()) return

        currentRates = RatesUIMapper.toViewModel(rates, emptyAllRateValues)
        ratesView.showRates(currentRates)
    }
}