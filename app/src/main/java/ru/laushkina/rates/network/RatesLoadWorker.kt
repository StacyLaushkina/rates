package ru.laushkina.rates.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.model.Rate
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.repository.RatesDbRepository
import ru.laushkina.rates.util.RatesLog

class RatesLoadWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    private var loadDismissible: Disposable? = null

    companion object {
        const val TAG = "[RatesLoadWorker]"
        const val BASE_RATE = "base_rate"
    }

    override fun doWork(): Result {
        val baseRate = inputData.getString(BASE_RATE)
        if (baseRate == null) {
            RatesLog.d(TAG, "Skip downloading rates. Base rate is unknown")
            return Result.failure()
        }

        RatesLog.d(TAG, "Started downloading rates for: $baseRate")
        val repository = RatesDbRepository(applicationContext)
        loadDismissible = RatesService(repository)
                .loadRates(baseRate)
                .doAfterSuccess { rates: List<Rate>? -> repository.save(rates) }
                .subscribe({ dispose() }) { dispose() }

        return Result.success()
    }

    private fun dispose() {
        loadDismissible?.dispose()
    }
}