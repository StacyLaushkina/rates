package ru.laushkina.rates.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import io.reactivex.disposables.Disposable
import ru.laushkina.rates.R
import ru.laushkina.rates.data.RatesDependencyOperator
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.ui.RateAdapter.ValueChangeListener
import ru.laushkina.rates.util.RatesLog

class RatesActivity : Activity(), RatesView, ValueChangeListener {
    companion object {
        private const val TAG = "RatesActivity"
    }

    private lateinit var ratesRecycler: RecyclerView
    private lateinit var ratesPresenter: RatesPresenter
    private lateinit var itemClickDisposable: Disposable
    private var rateAdapter: RateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)
        ratesRecycler = findViewById(R.id.rates)
        val service = RatesService(
                RatesDependencyOperator.getDBDataSource(getAppContext()),
                RatesDependencyOperator.getNetworkDataSource(),
                RatesDependencyOperator.getInMemoryDataSource()
        )

        ratesPresenter = RatesPresenter(service, WorkManager.getInstance(getAppContext()), this)
        ratesPresenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        ratesPresenter.onDestroy()
        itemClickDisposable.dispose()
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun showRates(rates: MutableList<RateViewModel>) {
        if (rateAdapter == null) {
            rateAdapter = RateAdapter(rates, this)
            itemClickDisposable = rateAdapter!!.getPositionClicks().subscribe { viewModel ->
                ratesPresenter.onRateSelected(viewModel)
                ratesRecycler.scrollToPosition(0)
            }
            ratesRecycler.adapter = rateAdapter
            ratesRecycler.layoutManager = GridLayoutManager(this, 1)
            ratesRecycler.setHasFixedSize(true)
        } else {
            ratesRecycler.post { rateAdapter!!.updateRates(rates) }
        }
    }

    override fun showError(throwable: Throwable) {
        RatesLog.e(TAG, "Common error", throwable)
        Toast.makeText(this, "Cannot update rates: " + throwable.message, Toast.LENGTH_LONG).show()
    }

    override fun onValueChange(rate: RateViewModel, value: String) {
        ratesPresenter.onRateValueChange(rate, value)
    }

    override fun beforeValueChange(rate: RateViewModel) {
        ratesPresenter.beforeRateValueChange()
    }

    override fun afterValueChange(rate: RateViewModel) {
        ratesPresenter.afterRateValueChange()
    }
}