package ru.laushkina.rates.di

import dagger.Component
import ru.laushkina.rates.ui.RatesPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [PresenterModule::class])
interface PresenterComponent {
    fun getRatesPresenter(): RatesPresenter
}