package ru.laushkina.rates.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.laushkina.rates.data.LastUpdateDataSource
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.database.RatesDBDataSource
import ru.laushkina.rates.data.memory.RatesInMemoryCache
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.data.sharedPrefs.LastUpdateSpDataSource

@Module
class DataModule {
    @Provides
    fun getLastUpdateDataSource(context: Context): LastUpdateDataSource {
        return LastUpdateSpDataSource(context)
    }

    @Provides
    fun getInMemoryDataSource(): RatesInMemoryCache {
        return RatesInMemoryCache()
    }

    @Provides
    fun getDBDataSource(context: Context): RatesDataSource {
        return RatesDBDataSource(context)
    }

    @Provides
    fun getNetworkDataSource(): RatesNetworkDataSource {
        return RatesNetworkDataSource()
    }
}