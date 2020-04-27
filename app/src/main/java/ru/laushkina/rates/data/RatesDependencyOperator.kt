package ru.laushkina.rates.data

import android.content.Context
import ru.laushkina.rates.data.database.RatesDBDataSource
import ru.laushkina.rates.data.memory.RatesInMemoryCache
import ru.laushkina.rates.data.network.RatesNetworkDataSource

class RatesDependencyOperator {
    companion object {
        fun getInMemoryDataSource(): RatesDataSource {
            return RatesInMemoryCache()
        }

        fun getDBDataSource(context: Context): RatesDataSource {
            return RatesDBDataSource(context)
        }

        fun getNetworkDataSource(): RatesNetworkDataSource {
            return RatesNetworkDataSource()
        }
    }
}