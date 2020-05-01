package ru.laushkina.rates.di

import dagger.Component
import ru.laushkina.rates.model.RatesService

@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun getRatesService(): RatesService
}