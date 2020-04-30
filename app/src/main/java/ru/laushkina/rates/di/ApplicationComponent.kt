package ru.laushkina.rates.di

import dagger.Component
import ru.laushkina.rates.model.RatesService
import ru.laushkina.rates.ui.RatesPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun getRatesPresenter(): RatesPresenter

    fun getRatesService(): RatesService
}