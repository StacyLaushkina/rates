package ru.laushkina.rates.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RateEntity::class], version = 1, exportSchema = false)
abstract class RatesDatabase: RoomDatabase() {
    companion object {
        private const val NAME = "rates_database"
        private val lock = Any()
        @Volatile
        private var instance: RatesDatabase? = null

        // TODO factory
        fun getInstance(context: Context): RatesDatabase {
            if (instance == null) {
                synchronized(lock) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context, RatesDatabase::class.java, NAME).build()
                    }
                }
            }
            return instance!!
        }
    }

    abstract fun rateDao(): RateDao
}