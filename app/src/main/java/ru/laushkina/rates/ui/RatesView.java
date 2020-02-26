package ru.laushkina.rates.ui;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.laushkina.rates.model.RateShortName;

public interface RatesView {
    void showRates(@NonNull List<RateViewModel> rates);

    void showError(@NonNull Throwable throwable);

    @NonNull
    Context getAppContext();

}
