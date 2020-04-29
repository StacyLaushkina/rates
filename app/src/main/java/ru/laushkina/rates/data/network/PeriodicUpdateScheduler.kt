package ru.laushkina.rates.data.network

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class PeriodicUpdateScheduler {
    companion object {
        private val UPDATE_INTERVAL_HOURS = TimeUnit.HOURS.toHours(2)

        fun scheduleNextUpdate(context: Context, amount: Float, shortName: String) {
            val rate = Data.Builder()
                    .putString(RatesLoadWorker.BASE_RATE_SHORT_NAME, shortName)
                    .putFloat(RatesLoadWorker.BASE_RATE_AMOUNT, amount)
                    .build()
            val mRequest = OneTimeWorkRequest.Builder(RatesLoadWorker::class.java)
                    .setInitialDelay(UPDATE_INTERVAL_HOURS, TimeUnit.HOURS)
                    .addTag(RatesLoadWorker.TAG)
                    .setInputData(rate)
                    .build()
            WorkManager.getInstance(context).enqueue(mRequest)
        }
    }
}