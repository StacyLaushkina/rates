package ru.laushkina.rates

import android.content.Context
import ru.laushkina.rates.data.LastUpdateDataSource
import ru.laushkina.rates.data.RatesDataSource
import ru.laushkina.rates.data.database.RatesDBDataSource
import ru.laushkina.rates.data.memory.RatesInMemoryCache
import ru.laushkina.rates.data.network.RatesNetworkDataSource
import ru.laushkina.rates.data.sharedPrefs.LastUpdateSpDataSource
import ru.laushkina.rates.model.RatesService

class RatesDependencyOperator {
    companion object {
        fun getRatesService(context: Context): RatesService {
            return RatesService(
                    context,
                    getDBDataSource(context),
                    getNetworkDataSource(),
                    getInMemoryDataSource(),
                    getLastUpdateDataSource(context)
            )
        }

        private fun getLastUpdateDataSource(context: Context): LastUpdateDataSource {
            return LastUpdateSpDataSource(context)
        }

        private fun getInMemoryDataSource(): RatesDataSource {
            return RatesInMemoryCache()
        }

        private fun getDBDataSource(context: Context): RatesDataSource {
            return RatesDBDataSource(context)
        }

        private fun getNetworkDataSource(): RatesNetworkDataSource {
            return RatesNetworkDataSource()
        }
    }
}