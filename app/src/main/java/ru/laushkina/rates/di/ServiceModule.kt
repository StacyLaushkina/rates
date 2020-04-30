package ru.laushkina.rates.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.laushkina.rates.data.LastUpdateDataSource
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.memory.RatesInMemoryCache
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.di.ContextModule
import ru.laushkina.rates.di.DataModule
import ru.laushkina.rates.model.RatesService

@Module(includes = [ContextModule::class, DataModule::class])
class ServiceModule {
    @Provides
    fun getRatesService(context: Context,
                        dbDataSource: RatesDataSource,
                        networkDataSource: RatesNetworkDataSource,
                        memoryDataSource: RatesInMemoryCache,
                        lastUpdateDataSource: LastUpdateDataSource): RatesService {
        return RatesService(context, dbDataSource, networkDataSource, memoryDataSource, lastUpdateDataSource)
    }
}