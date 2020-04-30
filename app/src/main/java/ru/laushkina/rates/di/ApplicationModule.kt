package ru.laushkina.rates.di

import dagger.Module
import dagger.Provides
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.ui.RatesPresenter
import ru.laushkina.rates.ui.RatesView

@Module(includes = [RatesViewModule::class, ServiceModule::class])
class ApplicationModule {
    @Provides
    fun getPresenter(ratesView: RatesView, service: RatesService): RatesPresenter {
        return RatesPresenter(ratesView, service)
    }
}