package ru.laushkina.rates.data.sharedPrefs

import android.content.Context
import ru.laushkina.rates.data.LastUpdateDataSource

class LastUpdateSpDataSource(context: Context): LastUpdateDataSource {
    companion object {
        private const val NAME = "last_update_prefs"
        private const val TIMESTAMP_NAME = "timestamp"
    }

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override fun save(lastUpdateTimestamp: Long) {
        sharedPreferences.edit().putLong(TIMESTAMP_NAME, lastUpdateTimestamp).apply()
    }

    override fun get(): Long {
        return sharedPreferences.getLong(TIMESTAMP_NAME, -1)
    }
}