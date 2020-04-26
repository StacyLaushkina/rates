package ru.laushkina.rates.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RateEntity::class], version = 1, exportSchema = false)
abstract class RatesDatabase: RoomDatabase() {
    companion object {
        private const val NAME = "rates_database"

        fun create(context: Context): RatesDatabase {
            return Room.databaseBuilder(context, RatesDatabase::class.java, NAME).build()

        }
    }

    abstract fun rateDao(): RateDao
}