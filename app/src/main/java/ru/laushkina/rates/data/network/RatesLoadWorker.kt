package ru.laushkina.rates.data.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.RatesDependencyOperator
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RateShortName
import ru.laushkina.rates.util.RatesLog

class RatesLoadWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    private var loadDismissible: Disposable? = null

    companion object {
        const val TAG = "[RatesLoadWorker]"
        const val BASE_RATE_SHORT_NAME = "base_rate_short_name"
        const val BASE_RATE_AMOUNT = "base_rate_amount"
    }

    override fun doWork(): Result {
        val baseRateShortName = inputData.getString(BASE_RATE_SHORT_NAME)
        val baseRateAmount = inputData.getFloat(BASE_RATE_AMOUNT, -1f)
        if (baseRateShortName == null || baseRateAmount == -1f) {
            RatesLog.d(TAG, "Skip downloading rates. Base rate is unknown")
            return Result.failure()
        }

        RatesLog.d(TAG, "Started downloading rates for: $baseRateShortName")
        val service = RatesDependencyOperator.getRatesService(applicationContext)

        loadDismissible = service
                .loadNewRates(Rate(RateShortName.parse(baseRateShortName), baseRateAmount, true))
                .subscribe({ dispose() }) { dispose() }

        return Result.success()
    }

    private fun dispose() {
        loadDismissible?.dispose()
    }
}