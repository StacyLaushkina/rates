package ru.laushkina.rates.model

import android.content.Context
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.network.PeriodicUpdateScheduler
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.util.RatesLog
import java.util.ArrayList

open class RatesService(private val context: Context,
                        private val dbDataSource: RatesDataSource,
                        private val networkDataSource: RatesNetworkDataSource,
                        private val memoryDataSource: RatesDataSource) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RatesService"
        private val DEFAULT_BASE_RATE = Rate(RateShortName.USD, 1f, true)

        private fun multiply(value: Float, original: List<Rate>): List<Rate> {
            val updatedValues: MutableList<Rate> = ArrayList()
            for (rate in original) {
                updatedValues.add(Rate(rate.shortName, rate.amount * value, rate.isBase))
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
    fun loadNewRates(): Maybe<List<Rate>> {
        return dbDataSource
                .getBaseRate()
                .defaultIfEmpty(DEFAULT_BASE_RATE)
                .subscribeOn(Schedulers.io())
                .flatMap { rate: Rate -> loadNewRates(rate) }
    }

    // Load rates from network, multiply it by current coefficient and save the result
    fun loadNewRates(baseRate: Rate): Maybe<List<Rate>> {
        return networkDataSource.getRates()
                .map { rates: List<Rate> ->
                    val newAmount = getBaseRateOrDefault(rates, baseRate.shortName).amount
                    val multipliedRates = multiply(baseRate.amount / newAmount, rates)
                    changeBaseRate(baseRate, multipliedRates)
                }
                .doAfterSuccess { rates: List<Rate>? ->
                    save(rates)
                    PeriodicUpdateScheduler.scheduleNextUpdate(context, baseRate.amount, baseRate.shortName.name)
                    dispose()
                }
                .doOnError { throwable: Throwable? ->
                    RatesLog.e(TAG, "Error in rates download", throwable)
                    dispose()
                }
    }

    private fun getBaseRateOrDefault(rates: List<Rate>, baseRateShortName: RateShortName): Rate {
        for (rate in rates) {
            if (rate.shortName == baseRateShortName) {
                return rate
            }
        }
        return DEFAULT_BASE_RATE
    }

    // Get data from local cache, re-calculate all rates and save result
    fun onRateValueChange(oldValue: Float, newValue: Float): Maybe<List<Rate>> {
        return loadFromInMemoryCache()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> calculateAndSaveRates(rates, newValue / oldValue) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    // Create new rates list with changed base rate and save the result
    fun onBaseRateChange(baseRate: Rate, currentRates: List<Rate>): Maybe<List<Rate>> {
        save(changeBaseRate(baseRate, currentRates))
        return loadFromInMemoryCache()
    }

    private fun maybeLoadFromNet(rates: List<Rate>): Maybe<List<Rate>> {
        return if (rates.isEmpty()) {
            loadNewRates()
        } else {
            memoryDataSource.save(rates)
            Maybe.just(rates).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun calculateAndSaveRates(rates: List<Rate>, value: Float): Maybe<List<Rate>> {
        val newRates = multiply(value, rates)
        save(newRates)
        return loadFromInMemoryCache()
    }

    private fun changeBaseRate(baseRate: Rate, currentRates: List<Rate>): List<Rate> {
        val newRates = ArrayList(currentRates)
        for (rate in newRates) {
            rate.isBase = (rate.shortName == baseRate.shortName)
        }
        newRates.sortByDescending { it.isBase }

        return newRates
    }

    private fun save(newRates: List<Rate>?) {
        memoryDataSource.save(newRates)
        dbDataSource.save(newRates)
    }

    private fun loadFromDbRepository(): Maybe<List<Rate>> {
        return dbDataSource.getRates()
    }

    private fun loadFromInMemoryCache(): Maybe<List<Rate>>  {
        return memoryDataSource.getRates()
    }

    private fun dispose() {
        compositeDisposable.dispose()
    }
}