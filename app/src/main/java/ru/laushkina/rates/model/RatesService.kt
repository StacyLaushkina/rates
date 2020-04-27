package ru.laushkina.rates.model

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
    }

    fun initialize(): Maybe<List<Rate>> {
        return loadFromDbRepository()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> maybeLoadFromNet(rates) }
    }

    fun loadRatesFromNetwork(baseRate: Rate): Maybe<List<Rate>> {
        return networkDataSource.getRates(baseRate.shortName.name)
                .map { rates: List<Rate> -> multiply(baseRate.amount, rates) }
                .doAfterSuccess { rates: List<Rate>? -> dbDataSource.save(rates) }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess { rates: List<Rate>? ->
                    memoryDataSource.save(rates)
                    dispose()
                }
                .doOnError { throwable: Throwable? ->
                    RatesLog.e(TAG, "Error in rates download", throwable)
                    dispose()
                }
    }

    fun onRateValueChange(value: Float): Maybe<List<Rate>> {
       return memoryDataSource.getRates()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> calculateAndSaveRates(rates, value) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onBaseRateChange(baseRate: Rate, currentRates: List<Rate>): Maybe<List<Rate>> {
        val newRates = ArrayList(currentRates)
        for (rate in newRates) {
            rate.isBase = (rate.shortName == baseRate.shortName)
        }
        newRates.sortByDescending { it.isBase }
        return saveAndLoadCache(newRates)
    }

    private fun multiply(value: Float, original: List<Rate>): List<Rate> {
        val updatedValues: MutableList<Rate> = ArrayList()
        for (rate in original) {
            updatedValues.add(Rate(rate.shortName, rate.amount * value, rate.isBase))
        }
        return updatedValues
    }

    private fun maybeLoadFromNet(rates: List<Rate>): Maybe<List<Rate>>  {
        return if (rates.isEmpty()) {
            dbDataSource
                    .getBaseRate()
                    .defaultIfEmpty(DEFAULT_BASE_RATE)
                    .flatMap { rate: Rate -> loadRatesFromNetwork(rate) }
        }
        else {
            memoryDataSource.save(rates)
            Maybe.just(rates).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun calculateAndSaveRates(rates: List<Rate>, value: Float): Maybe<List<Rate>> {
        val newRates = multiply(value, rates)
        return saveAndLoadCache(newRates)
    }

    private fun loadFromDbRepository(): Maybe<List<Rate>> {
        return dbDataSource.getRates()
    }

    private fun saveAndLoadCache(newRates: List<Rate>): Maybe<List<Rate>> {
        memoryDataSource.save(newRates)
        dbDataSource.save(newRates)
        return memoryDataSource.getRates()
    }

    private fun dispose() {
        compositeDisposable.dispose()
    }
}