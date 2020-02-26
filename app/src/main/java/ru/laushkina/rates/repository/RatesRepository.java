package ru.laushkina.rates.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import ru.laushkina.rates.model.Rate;

public interface RatesRepository {

    void save(@NonNull List<Rate> rates);

    @NonNull
    Single<List<Rate>> getRates();

    @NonNull
    Maybe<Rate> getBaseRate();
}
