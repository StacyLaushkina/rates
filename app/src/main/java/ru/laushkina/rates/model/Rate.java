package ru.laushkina.rates.model;

import androidx.annotation.NonNull;

public class Rate {
    @NonNull
    private final String shortName;
    @NonNull
    private final Float amount;
    private final boolean isBase;

    public Rate(@NonNull String shortName, @NonNull Float amount, boolean isBase) {
        this.shortName = shortName;
        this.amount = amount;
        this.isBase = isBase;
    }

    @NonNull
    public String getShortName() {
        return shortName;
    }

    @NonNull
    public Float getAmount() {
        return amount;
    }

    public boolean isBase() {
        return isBase;
    }
}
