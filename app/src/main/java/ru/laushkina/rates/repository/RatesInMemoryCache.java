package ru.laushkina.rates.repository;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.laushkina.rates.model.Rate;

public class RatesInMemoryCache {
    private static final Object lock = new Object();
    private static volatile RatesInMemoryCache INSTANCE;

    @NonNull
    private Subject<List<Rate>> observable = PublishSubject.create();

    public static RatesInMemoryCache getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new RatesInMemoryCache();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    public Subject<List<Rate>> getCachedRates() {
        return observable;
    }

    public void setCachedRates(@NonNull List<Rate> rates) {
        observable.onNext(rates);
    }
}
