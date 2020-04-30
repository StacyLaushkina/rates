package ru.laushkina.rates.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.laushkina.rates.ContextModule
import ru.laushkina.rates.DaggerApplicationComponent
import ru.laushkina.rates.R
import ru.laushkina.rates.RatesViewModule
import ru.laushkina.rates.util.RatesLog
import javax.inject.Inject

class RatesActivity : Activity(), RatesView, RateAdapter.ValueChangeListener, RateAdapter.RateClickListener {
    companion object {
        private const val TAG = "RatesActivity"
    }

    private lateinit var ratesRecycler: RecyclerView
    @Inject lateinit var ratesPresenter: RatesPresenter
    private var rateAdapter: RateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)
        ratesRecycler = findViewById(R.id.rates)

        ratesPresenter = DaggerApplicationComponent.builder()
                .contextModule(ContextModule(this))
                .ratesViewModule(RatesViewModule(this))
                .build().getRatesPresenter()

        ratesPresenter.onCreate()

        val updateFab: FloatingActionButton = findViewById(R.id.update_fab)
        updateFab.setOnClickListener { ratesPresenter.onUpdateRequested() }
    }

    override fun onDestroy() {
        super.onDestroy()
        ratesPresenter.onDestroy()
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun showRates(rates: MutableList<RateViewModel>) {
        if (rateAdapter == null) {
            rateAdapter = RateAdapter(rates, this, this)
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

    override fun showTooEarlyForNewUpdate() {
        Toast.makeText(this, "Too early for new update", Toast.LENGTH_LONG).show()
    }

    override fun onValueChange(rate: RateViewModel, value: String) {
        ratesPresenter.onRateValueChange(rate, value)
    }

    override fun onClicked(position: Int, rate: RateViewModel) {
        ratesRecycler.scrollToPosition(0)
        ratesPresenter.onRateSelected(rate)
    }
}