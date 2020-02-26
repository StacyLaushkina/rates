package ru.laushkina.rates.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import io.reactivex.disposables.Disposable;
import ru.laushkina.rates.repository.RatesDbRepository;
import ru.laushkina.rates.model.RatesService;
import ru.laushkina.rates.util.RatesLog;

public class RatesLoadWorker extends Worker {
    public static final String TAG = "RatesLoadWorker";
    public static final String BASE_RATE = "base_rate";

    @Nullable
    private Disposable loadDismissable = null;

    public RatesLoadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String baseRate = getInputData().getString(BASE_RATE);
        if (baseRate == null) {
            RatesLog.d(TAG, "Skip downloading rates. Base rate is unknown");
            return Result.failure();
        }

        RatesLog.d(TAG, "Started downloading rates for: " + baseRate);
        final RatesDbRepository repository = new RatesDbRepository(getApplicationContext());
        loadDismissable = new RatesService(repository)
                .loadRates(baseRate)
                .doAfterSuccess(repository::save)
                .subscribe(success -> dispose(), error -> dispose());

        return Result.success();
    }

    private void dispose() {
        if (loadDismissable != null) {
            loadDismissable.dispose();
        }
    }
}
