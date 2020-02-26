package ru.laushkina.rates.repository;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;

import ru.laushkina.rates.model.Rate;

@Entity( tableName = "rates")
public class RateEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String shortName;
    public Float amount;
    public boolean isBase;

    public RateEntity(int id, @NonNull String shortName, @NonNull Float amount, boolean isBase) {
        this.id = id;
        this.shortName = shortName;
        this.amount = amount;
        this.isBase = isBase;
    }

    public RateEntity(@NonNull Rate rate) {
        this(new Random().nextInt(), rate.getShortName(), rate.getAmount(), rate.isBase());
    }
}
