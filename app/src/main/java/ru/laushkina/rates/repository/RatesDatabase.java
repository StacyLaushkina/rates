package ru.laushkina.rates.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { RateEntity.class }, version = 1, exportSchema = false)
public abstract class RatesDatabase extends RoomDatabase {
    private static final String NAME = "rates_database";

    private static volatile RatesDatabase instance = null;
    private static final Object lock = new Object();

    public static RatesDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized(lock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, RatesDatabase.class, NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract RateDao rateDao();
}
