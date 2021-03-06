package ru.laushkina.rates.data.network

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class PeriodicUpdateScheduler {
    companion object {
        fun scheduleNextUpdate(context: Context, initialDelay: Long, amount: Float, shortName: String) {
            val manager = WorkManager.getInstance(context)
            manager.cancelAllWorkByTag(RatesLoadWorker.TAG)

            val rate = Data.Builder()
                    .putString(RatesLoadWorker.BASE_RATE_SHORT_NAME, shortName)
                    .putFloat(RatesLoadWorker.BASE_RATE_AMOUNT, amount)
                    .build()
            val mRequest = OneTimeWorkRequest.Builder(RatesLoadWorker::class.java)
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .addTag(RatesLoadWorker.TAG)
                    .setInputData(rate)
                    .build()
            manager.enqueue(mRequest)
        }
    }
}