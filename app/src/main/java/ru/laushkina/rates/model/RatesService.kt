package ru.laushkina.rates.model

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.util.RatesLog
import java.util.ArrayList

open class RatesService(private val dbDataSource: RatesDataSource,
                        private val networkDataSource: RatesNetworkDataSource,
                        private val memoryDataSource: RatesDataSource) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RatesService"
        private val DEFAULT_BASE_RATE = Rate(RateShortName.EUR, 1f, true)

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

    // Load rates with baseRate, multiply it by current coefficient and save the result
    fun loadRatesFromNetwork(baseRate: Rate): Maybe<List<Rate>> {
        return networkDataSource.getRates(baseRate.shortName.name)
                .map { rates: List<Rate> -> multiply(baseRate.amount, rates) }
                .doAfterSuccess { rates: List<Rate>? ->
                    save(rates)
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
                .flatMap { rates: List<Rate> -> calculateAndSaveRates(rates, newValue / oldValue) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    // Create new rates list with changed base rate and save the result
    fun onBaseRateChange(baseRate: Rate, currentRates: List<Rate>): Maybe<List<Rate>> {
        val newRates = ArrayList(currentRates)
        for (rate in newRates) {
            rate.isBase = (rate.shortName == baseRate.shortName)
        }
        newRates.sortByDescending { it.isBase }
        save(newRates)
        return loadFromInMemoryCache()
    }

    private fun maybeLoadFromNet(rates: List<Rate>): Maybe<List<Rate>> {
        return if (rates.isEmpty()) {
            dbDataSource
                    .getBaseRate()
                    .defaultIfEmpty(DEFAULT_BASE_RATE)
                    .flatMap { rate: Rate -> loadRatesFromNetwork(rate) }
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