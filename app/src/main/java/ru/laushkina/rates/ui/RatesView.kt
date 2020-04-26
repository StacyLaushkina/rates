package ru.laushkina.rates.ui

import android.content.Context

interface RatesView {
    fun showRates(rates: MutableList<RateViewModel>)

    fun showError(throwable: Throwable)

    fun getAppContext(): Context
}