package ru.laushkina.rates.ui

import android.util.Log
import android.util.Pair
import androidx.annotation.VisibleForTesting
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.data.network.RatesLoadWorker
import ru.laushkina.rates.ui.RateFullName.Companion.getFullName
import java.util.*
import java.util.concurrent.TimeUnit

class RatesPresenter(private val ratesService: RatesService,
                     private val workManager: WorkManager,
                     private val ratesView: RatesView) {

    @VisibleForTesting
    var initializeDisposable: Disposable? = null

    private var baseRate: RateShortName? = null

    @VisibleForTesting
    var currentRates: MutableList<RateViewModel> = arrayListOf()

    private var multiplier = 1f
    private var emptyAllRateValues = false

    companion object {
        private const val TAG = "RatesPresenter"
        private val UPDATE_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(1)
    }

    fun onCreate() {
        initializeDisposable = ratesService.initialize().subscribe(
                {
                    rates: List<Rate> -> this.onRatesLoaded(rates)
                },
                ratesView::showError
        )
    }

    fun onDestroy() {
        initializeDisposable?.dispose()
    }

    fun onRateSelected(selectedRate: RateViewModel) {
        baseRate = RateShortName.parse(selectedRate.shortName)
    }

    fun beforeRateValueChange() {
        // TODO Looks like not needed anymore
        workManager.cancelAllWorkByTag(RatesLoadWorker.TAG)
    }

    fun afterRateValueChange() {
        scheduleNextUpdate()
    }

    fun onRateValueChange(rate: RateViewModel, value: String) {
        var floatValue = if (value.isEmpty()) null else java.lang.Float.valueOf(value)
        if (floatValue == null || floatValue == 0f) {
            emptyAllRateValues = true
            floatValue = 1f
        } else {
            emptyAllRateValues = false
        }
        currentRates = multiply(currentRates, floatValue / rate.amount)
        multiplier = floatValue
        ratesView.showRates(currentRates)
    }

    private fun multiply(original: MutableList<RateViewModel>, value: Float): MutableList<RateViewModel> {
        val updatedValues: MutableList<RateViewModel> = ArrayList()
        for (viewModel in original) {
            updatedValues.add(RateViewModel(
                    viewModel.imageId,
                    viewModel.name,
                    viewModel.shortName,
                    value * viewModel.amount,
                    !emptyAllRateValues)
            )
        }
        return updatedValues
    }

    private fun onRatesLoaded(rates: List<Rate>) {
        if (rates.isEmpty()) return

        val baseRate = rates[0]
        this.baseRate = baseRate.shortName
        if (!baseRate.isBase) {
            Log.e(TAG, "First rate is not a base one: " + this.baseRate)
        }
        currentRates = getRateViewModel(rates, multiplier)
        ratesView.showRates(currentRates)
        scheduleNextUpdate()
    }

    private fun getRateViewModel(rates: List<Rate>, multiplier: Float): MutableList<RateViewModel> {
        val result: MutableList<RateViewModel> = ArrayList(rates.size)
        var nameImagePair: Pair<Int, Int>
        for (rate in rates) {
            nameImagePair = getFullName(rate.shortName)
            result.add(RateViewModel(
                    nameImagePair.second,
                    nameImagePair.first,
                    rate.shortName.name,
                    multiplier * rate.amount,
                    !emptyAllRateValues))
        }
        return result
    }

    private fun scheduleNextUpdate() {
        val rate = Data.Builder().putString(RatesLoadWorker.BASE_RATE, baseRate?.name).build()
        val mRequest = OneTimeWorkRequest.Builder(RatesLoadWorker::class.java)
                .setInitialDelay(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .addTag(RatesLoadWorker.TAG)
                .setInputData(rate)
                .build()
        workManager.enqueue(mRequest)
    }
}