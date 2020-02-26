package ru.laushkina.rates.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.laushkina.rates.R;
import ru.laushkina.rates.model.RatesService;
import ru.laushkina.rates.model.RateShortName;
import ru.laushkina.rates.repository.RatesDbRepository;
import ru.laushkina.rates.util.RatesLog;

public class RatesActivity extends Activity implements RatesView, RateAdapter.ValueChangeListener {
    private static final String TAG = "RatesActivity";
    private RecyclerView ratesRecycler;
    private RatesPresenter ratesPresenter;
    private Disposable itemClickDisposable;
    private RateAdapter rateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        ratesRecycler = findViewById(R.id.rates);

        RatesService service = new RatesService(new RatesDbRepository(getAppContext()));
        ratesPresenter = new RatesPresenter(service, WorkManager.getInstance(getAppContext()), this);
        ratesPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ratesPresenter.onDestroy();
        if (itemClickDisposable != null) {
            itemClickDisposable.dispose();
        }
    }

    @Override
    public void showRates(@NonNull List<RateViewModel> rates) {
        if (rateAdapter == null) {
            rateAdapter = new RateAdapter(rates, this);
            itemClickDisposable = rateAdapter.getPositionClicks().subscribe(viewModel -> {
                ratesPresenter.onRateSelected(viewModel);
                ratesRecycler.scrollToPosition(0);
            });

            ratesRecycler.setAdapter(rateAdapter);
            ratesRecycler.setLayoutManager(new GridLayoutManager(this, 1));
            ratesRecycler.setHasFixedSize(true);
        } else {
            ratesRecycler.post(() -> {
                rateAdapter.updateRates(rates);
            });
        }
    }

    @Override
    public void showError(@NonNull Throwable throwable) {
        RatesLog.e(TAG, "", throwable);
        Toast.makeText(this, "Cannot update rates: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void beforeValueChange(@NonNull RateViewModel rate) {
        ratesPresenter.beforeRateValueChange();
    }

    @Override
    public void afterValueChange(@NonNull RateViewModel rate) {
        ratesPresenter.afterRateValueChange();
    }

    @Override
    public void onValueChange(@NonNull RateViewModel rate, @NonNull String value) {
        ratesPresenter.onRateValueChange(rate, value);
    }
}
