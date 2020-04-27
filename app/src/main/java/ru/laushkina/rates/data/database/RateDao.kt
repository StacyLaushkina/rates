package ru.laushkina.rates.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface RateDao {
    @Query("SELECT * FROM rates ORDER BY isBase DESC")
    fun loadAll(): Maybe<List<RateEntity>>

    @Query("SELECT * FROM rates WHERE isBase limit 1")
    fun loadBaseRate(): Maybe<RateEntity>

    @Insert
    fun save(rates: List<RateEntity>?)

    @Query("DELETE FROM rates")
    fun truncate()
}