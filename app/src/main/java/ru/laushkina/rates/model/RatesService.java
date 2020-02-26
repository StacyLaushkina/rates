package ru.laushkina.rates.model;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.laushkina.rates.network.RatesApiFactory;
import ru.laushkina.rates.network.RatesMapper;
import ru.laushkina.rates.repository.RatesInMemoryCache;
import ru.laushkina.rates.repository.RatesRepository;
import ru.laushkina.rates.util.RatesLog;

public class RatesService {
    private static final String TAG = "RatesService";
    private static final String DEFAULT_BASE_RATE = RateShortName.EUR;
    @NonNull
    private final RatesRepository ratesRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RatesService(@NonNull RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    @NonNull
    public Disposable initRatesCache() {
        return loadFromDbRepository()
                .doAfterSuccess(rates -> {
                    if (rates.isEmpty()) {
                        requestRatesLoad();
                    } else {
                        RatesInMemoryCache.getInstance().setCachedRates(rates);
                    }
                })
                .subscribe();
    }

    public void requestRatesLoad() {
        compositeDisposable.add(ratesRepository.getBaseRate()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        rate -> compositeDisposable.add(loadRates(rate.getShortName()).subscribe()),
                        error -> {
                        },
                        () -> compositeDisposable.add(loadRates(DEFAULT_BASE_RATE)
                                .subscribe(rates -> {}, throwable -> RatesLog.e(TAG, "", throwable)))
                ));
    }

    public Single<List<Rate>> loadRates(@NonNull String accessKey) {
        return RatesApiFactory.create()
                .getRates("adfba205d005b83742c46e9160cc6084")
                .subscribeOn(Schedulers.io())
                .map(RatesMapper::mapToRates)
                .doAfterSuccess(ratesRepository::save) // Non UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(rates -> {
                    RatesInMemoryCache.getInstance().setCachedRates(rates); // UI thread
                    compositeDisposable.dispose();
                })
                .doOnError(throwable -> RatesLog.e(TAG, "Error in rates download", throwable));
    }

    @NonNull
    private Single<List<Rate>> loadFromDbRepository() {
        return ratesRepository.getRates().subscribeOn(Schedulers.io());
    }
}
