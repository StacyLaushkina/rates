package ru.laushkina.rates.model

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.util.RatesLog

open class RatesService(private val dbDataSource: RatesDataSource,
                        private val networkDataSource: RatesNetworkDataSource,
                        private val memoryDataSource: RatesDataSource) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RatesService"
        private val DEFAULT_BASE_RATE = Rate(RateShortName.EUR, 0f, true)
    }

    fun initialize(): Maybe<List<Rate>> {
        return loadFromDbRepository()
                .subscribeOn(Schedulers.io())
                .flatMap { rates: List<Rate> -> maybeLoadFromNet(rates) }
    }

    fun loadRatesFromNetwork(baseRate: String): Maybe<List<Rate>> {
        return networkDataSource.getRates(baseRate)
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

    private fun maybeLoadFromNet(rates: List<Rate>): Maybe<List<Rate>>  {
        return if (rates.isEmpty()) {
            dbDataSource
                    .getBaseRate()
                    .defaultIfEmpty(DEFAULT_BASE_RATE)
                    .flatMap { rate: Rate -> loadRatesFromNetwork(rate.shortName.name) }
        }
        else {
            memoryDataSource.save(rates)
            Maybe.just(rates)
        }
    }

    private fun loadFromDbRepository(): Maybe<List<Rate>> {
        return dbDataSource.getRates()
    }

    private fun dispose() {
        compositeDisposable.dispose()
    }
}