package ru.laushkina.rates.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import ru.laushkina.rates.model.Rate;

public class RatesDbRepository implements RatesRepository {
    private final RatesDatabase ratesDatabase;

    public RatesDbRepository(@NonNull Context context) {
        ratesDatabase = RatesDatabase.getInstance(context);
    }

    @Override
    public void save(@NonNull List<Rate> rates) {
        ratesDatabase.getTransactionExecutor().execute(() -> {
            this.ratesDatabase.rateDao().truncate();
            this.ratesDatabase.rateDao().save(mapToEntity(rates));
        });
    }

    @NonNull
    @Override
    public Single<List<Rate>> getRates() {
        return ratesDatabase.rateDao()
                .loadAll()
                .map(RatesDbRepository::mapToRate);
    }

    @NonNull
    @Override
    public Maybe<Rate> getBaseRate() {
        return ratesDatabase.rateDao()
                .loadBaseRate()
                .map(entity -> new Rate(entity.shortName, entity.amount, entity.isBase));
    }

    @NonNull
    private static List<RateEntity> mapToEntity(@NonNull List<Rate> rates) {
        List<RateEntity> result = new ArrayList<>(rates.size());
        for (Rate rate : rates) {
            result.add(new RateEntity(rate));
        }
        return result;
    }

    @NonNull
    private static List<Rate> mapToRate(@NonNull List<RateEntity> entities) {
        List<Rate> result = new ArrayList<>(entities.size());
        for (RateEntity entity : entities) {
            result.add(new Rate(entity.shortName, entity.amount, entity.isBase));
        }
        return result;
    }
}
