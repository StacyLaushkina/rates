package ru.laushkina.rates.ui;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import ru.laushkina.rates.model.Rate;
import ru.laushkina.rates.network.RatesLoadWorker;
import ru.laushkina.rates.model.RateShortName;
import ru.laushkina.rates.model.RatesService;
import ru.laushkina.rates.repository.RatesInMemoryCache;

class RatesPresenter {
    private static final String TAG = "RatesPresenter";
    private final static long UPDATE_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(1);
    @NonNull
    private final RatesService ratesService;
    @NonNull
    private final RatesView ratesView;

    @Nullable
    @VisibleForTesting
    Disposable loadDisposable;
    @Nullable
    @VisibleForTesting
    Disposable cacheDisposable;
    @Nullable
    @RateShortName.Name
    private String baseRate;
    @NonNull
    private final WorkManager workManager;

    @Nullable
    @VisibleForTesting
    List<RateViewModel> currentRates;

    private float multiplier = 1f;
    private boolean emptyAllRateValues = false;

    RatesPresenter(@NonNull RatesService ratesService,
                   @NonNull WorkManager workManager,
                   @NonNull RatesView ratesView) {
        this.ratesService = ratesService;
        this.workManager = workManager;
        this.ratesView = ratesView;
    }

    void onCreate() {
        cacheDisposable = RatesInMemoryCache.getInstance().getCachedRates().subscribe(this::onRatesLoaded, ratesView::showError);
        loadDisposable = ratesService.initRatesCache();
    }

    void onDestroy() {
        disposeLoad();
        if (cacheDisposable != null) {
            cacheDisposable.dispose();
        }
    }

    void onRateSelected(@NonNull RateViewModel selectedRate) {
        this.baseRate = selectedRate.getShortName();
        immediatelyLoadRates(this.baseRate);
    }

    void beforeRateValueChange() {
        disposeLoad();
        workManager.cancelAllWorkByTag(RatesLoadWorker.TAG);
    }

    void afterRateValueChange() {
        scheduleNextUpdate();
    }

    void onRateValueChange(@NonNull RateViewModel rate, @NonNull String value) {
        if (currentRates == null) {
            Log.e(TAG, "Trying to update rate values, but local cache is empty");
            return;
        }

        Float floatValue = value.length() == 0 ? null : Float.valueOf(value);

        if (floatValue == null || floatValue == 0) {
            emptyAllRateValues = true;
            floatValue = 1f;
        } else {
            emptyAllRateValues = false;
        }

        currentRates = multiply(currentRates, floatValue / rate.getAmount());
        multiplier = floatValue;

        ratesView.showRates(currentRates);
    }

    @NonNull
    private List<RateViewModel> multiply(@NonNull List<RateViewModel> original, float value) {
        List<RateViewModel> updatedValues = new ArrayList<>();
        for (RateViewModel viewModel : original) {
            updatedValues.add(new RateViewModel(
                    viewModel.getImageId(),
                    viewModel.getName(),
                    viewModel.getShortName(),
                    value * viewModel.getAmount(),
                    !emptyAllRateValues)
            );
        }
        return updatedValues;
    }

    private void immediatelyLoadRates(@NonNull String baseRate) {
        disposeLoad();
        workManager.cancelAllWorkByTag(RatesLoadWorker.TAG);

        loadDisposable = ratesService
                .loadRates(baseRate)
                .subscribe(this::onRatesLoaded, ratesView::showError);
    }

    private void onRatesLoaded(@NonNull List<Rate> rates) {
        disposeLoad();

        Rate baseRate = rates.get(0);
        this.baseRate = baseRate.getShortName();
        if (!baseRate.isBase()) {
            Log.e(TAG, "First rate is not a base one: " + this.baseRate);
        }

        currentRates = getRateViewModel(rates, multiplier);
        ratesView.showRates(currentRates);
        scheduleNextUpdate();
    }

    private void scheduleNextUpdate() {
        Data rate = new Data.Builder().putString(RatesLoadWorker.BASE_RATE, baseRate).build();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(RatesLoadWorker.class)
                .setInitialDelay(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .addTag(RatesLoadWorker.TAG)
                .setInputData(rate)
                .build();
        workManager.enqueue(mRequest);
    }

    @NonNull
    private List<RateViewModel> getRateViewModel(@NonNull List<Rate> rates, float multiplier) {
        List<RateViewModel> result = new ArrayList<>(rates.size());

        Pair<Integer, Integer> nameImagePair;
        for (Rate rate : rates) {
            nameImagePair = RateFullName.Companion.getFullName(rate.getShortName());
            result.add(new RateViewModel(
                    nameImagePair.second,
                    nameImagePair.first,
                    rate.getShortName(),
                    multiplier * rate.getAmount(),
                            !emptyAllRateValues));
        }

        return result;
    }

    private void disposeLoad() {
        if (loadDisposable != null && !loadDisposable.isDisposed()) {
            loadDisposable.dispose();
        }
    }
}
