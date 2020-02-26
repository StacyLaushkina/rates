package ru.laushkina.rates.ui;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

class RateViewModel {
    @DrawableRes
    private final int imageId;
    @StringRes
    private final int name;
    @NonNull
    private final String shortName;
    private final float amount;

    // If true, show real amount. If false, show null
    private final boolean showAmount;

    RateViewModel(@DrawableRes int imageId,
                  @StringRes int name,
                  @NonNull String shortName,
                  float amount,
                  boolean showAmount) {
        this.imageId = imageId;
        this.name = name;
        this.shortName = shortName;
        this.amount = amount;
        this.showAmount = showAmount;
    }

    @DrawableRes
    int getImageId() {
        return imageId;
    }

    @StringRes
    int getName() {
        return name;
    }

    @NonNull
    String getShortName() {
        return shortName;
    }

    float getAmount() {
        return amount;
    }

    boolean showAmount() {
        return showAmount;
    }
}
