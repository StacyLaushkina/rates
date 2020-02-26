package ru.laushkina.rates.repository;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface RateDao {
    @Query("SELECT * FROM rates ORDER BY isBase DESC")
    Single<List<RateEntity>> loadAll();

    @Query("SELECT * FROM rates WHERE isBase limit 1")
    Maybe<RateEntity> loadBaseRate();

    @Insert
    void save(@NonNull List<RateEntity> rates);

    @Query("DELETE FROM rates")
    void truncate();
}
