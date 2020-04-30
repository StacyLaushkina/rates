package ru.laushkina.rates.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.laushkina.rates.ui.RatesView

@Module
class RatesViewModule(private val ratesView: RatesView) {
    @Provides
    fun ratesView(): RatesView {
        return ratesView
    }
}