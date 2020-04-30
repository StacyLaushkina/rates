package ru.laushkina.rates.model

import android.content.Context
import androidx.annotation.VisibleForTesting
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.data.LastUpdateDataSource
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.memory.RatesInMemoryCache
import ru.laushkina.rates.data.network.PeriodicUpdateScheduler
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.util.RatesLog
import java.util.ArrayList
import java.util.concurrent.TimeUnit

open class RatesService(private val context: Context,
                        private val dbDataSource: RatesDataSource,
                        private val networkDataSource: RatesNetworkDataSource,
                        private val memoryDataSource: RatesDataSource,
                        private val lastUpdateDataSource: LastUpdateDataSource) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RatesService"
        private val DEFAULT_BASE_RATE = Rate(RateShortName.USD, 1f, true)
        private val UPDATE_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(4)
        private val UPDATE_MIN_UPDATE_PROHIBIT_DURATION_SECONDS = TimeUnit.MINUTES.toSeconds(30)

        @VisibleForTesting
        fun multiply(oldValue: Float, newValue: Float, original: List<Rate>): List<Rate> {
            val multiplier = newValue / oldValue
            val updatedValues: MutableList<Rate> = ArrayList()
            for (rate in original) {
                updatedValues.add(Rate(rate.shortName, rate.amount * multiplier, rate.isBase))
            }
            return updatedValues
        }
    }

    // Initialize local cache with values from DB or load from network if DB is empty
    fun initialize(): Maybe<List<Rate>> {
        return loadFromDbRepository()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> maybeLoadFromNet(rates) }
    }

    // Load rates from network, multiply it by current coefficient and save the result
    fun requestLoadRates(): Maybe<List<Rate>> {
        val lastUpdateTime = lastUpdateDataSource.get()
        val diff = System.currentTimeMillis() - lastUpdateTime
        if (TimeUnit.MILLISECONDS.toSeconds(diff) >= UPDATE_MIN_UPDATE_PROHIBIT_DURATION_SECONDS) {
            return loadNewRates()
        }
        return Maybe.empty()
    }

    // Load rates from network, multiply it by current coefficient and save the result
    fun loadNewRates(baseRate: Rate): Maybe<List<Rate>> {
        return networkDataSource.getRates()
                .map { rates: List<Rate> ->
                    val serverAmount = getBaseRateOrDefault(rates, baseRate.shortName).amount
                    // We need to change values from server to adapt local coefficients, so old value is the one from network
                    val multipliedRates = multiply(serverAmount, baseRate.amount, rates)
                    changeBaseRate(baseRate, multipliedRates)
                }
                .doAfterSuccess { rates: List<Rate>? ->
                    save(rates)
                    PeriodicUpdateScheduler.scheduleNextUpdate(context, UPDATE_INTERVAL_SECONDS, baseRate.amount, baseRate.shortName.name)
                    lastUpdateDataSource.save(System.currentTimeMillis())
                    dispose()
                }
                .doOnError { throwable: Throwable? ->
                    RatesLog.e(TAG, "Error in rates download", throwable)
                    dispose()
                }
    }

    // Get data from local cache, re-calculate all rates and save result
    fun onRateValueChange(oldValue: Float, newValue: Float): Maybe<List<Rate>> {
        return loadFromInMemoryCache()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> calculateAndSaveRates(rates, oldValue, newValue) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    // Create new rates list with changed base rate and save the result
    fun onBaseRateChange(baseRate: Rate, currentRates: List<Rate>): Maybe<List<Rate>> {
        save(changeBaseRate(baseRate, currentRates))
        return loadFromInMemoryCache()
    }

    @VisibleForTesting
    fun getBaseRateOrDefault(rates: List<Rate>, baseRateShortName: RateShortName): Rate {
        for (rate in rates) {
            if (rate.shortName == baseRateShortName) {
                return rate
            }
        }
        return DEFAULT_BASE_RATE
    }

    @VisibleForTesting
    fun calculateAndSaveRates(rates: List<Rate>, oldValue: Float, newValue: Float): Maybe<List<Rate>> {
        val newRates = multiply(oldValue, newValue, rates)
        save(newRates)
        return loadFromInMemoryCache()
    }

    @VisibleForTesting
    fun changeBaseRate(baseRate: Rate, currentRates: List<Rate>): List<Rate> {
        val newRates = ArrayList(currentRates)
        for (rate in newRates) {
            rate.isBase = (rate.shortName == baseRate.shortName)
        }
        newRates.sortByDescending { it.isBase }

        return newRates
    }

    @VisibleForTesting
    fun save(newRates: List<Rate>?) {
        memoryDataSource.save(newRates)
        dbDataSource.save(newRates)
    }

    private fun maybeLoadFromNet(rates: List<Rate>): Maybe<List<Rate>> {
        return if (rates.isEmpty()) {
            loadNewRates()
        } else {
            memoryDataSource.save(rates)
            Maybe.just(rates).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun loadNewRates(): Maybe<List<Rate>> {
        return dbDataSource
                .getBaseRate()
                .defaultIfEmpty(DEFAULT_BASE_RATE)
                .subscribeOn(Schedulers.io())
                .flatMap { rate: Rate -> loadNewRates(rate) }
    }

    private fun loadFromDbRepository(): Maybe<List<Rate>> {
        return dbDataSource.getRates()
    }

    private fun loadFromInMemoryCache(): Maybe<List<Rate>> {
        return memoryDataSource.getRates()
    }

    private fun dispose() {
        compositeDisposable.dispose()
    }
}