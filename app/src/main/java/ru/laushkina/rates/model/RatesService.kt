package ru.laushkina.rates.model

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.laushkina.rates.network.RatesApiFactory
import ru.laushkina.rates.network.RatesMapper
import ru.laushkina.rates.network.RatesResponse
import ru.laushkina.rates.repository.RatesInMemoryCache
import ru.laushkina.rates.repository.RatesRepository
import ru.laushkina.rates.util.RatesLog

open class RatesService(private val ratesRepository: RatesRepository) {
    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RatesService"
        private val DEFAULT_BASE_RATE = RateShortName.EUR.name
    }

    fun initRatesCache(): Disposable {
        return loadFromDbRepository()
                .doAfterSuccess { rates: List<Rate> ->
                    if (rates.isEmpty()) {
                        requestRatesLoad()
                    } else {
                        RatesInMemoryCache.getInstance().setCachedRates(rates)
                    }
                }
                .subscribe()
    }

    fun requestRatesLoad() {
        compositeDisposable.add(
                ratesRepository.getBaseRate()
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { rate: Rate -> compositeDisposable.add(loadRates(rate.shortName.name).subscribe()) },
                                { throwable: Throwable -> RatesLog.e(TAG, "Could not get base rate", throwable) }
                        )
                        {
                            compositeDisposable.add(
                                    loadRates(DEFAULT_BASE_RATE)
                                            .subscribe(
                                                    { },
                                                    { throwable: Throwable -> RatesLog.e(TAG, "Could not load rate", throwable) }
                                            )
                            )
                        })
    }

    fun loadRates(accessKey: String?): Maybe<List<Rate>?> {
        return RatesApiFactory.create()
                .getRates("adfba205d005b83742c46e9160cc6084")
                .subscribeOn(Schedulers.io())
                .map { response: RatesResponse -> RatesMapper.mapToRates(response) }
                .doAfterSuccess { rates: List<Rate>? -> ratesRepository.save(rates) } // Non UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess { rates: List<Rate>? ->
                    RatesInMemoryCache.getInstance().setCachedRates(rates) // UI thread
                    compositeDisposable.dispose()
                }
                .doOnError { throwable: Throwable? -> RatesLog.e(TAG, "Error in rates download", throwable!!) }
    }

    private fun loadFromDbRepository(): Maybe<List<Rate>> {
        return ratesRepository.getRates().subscribeOn(Schedulers.io())
    }

}