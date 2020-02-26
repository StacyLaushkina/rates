package ru.laushkina.rates.util;

import androidx.annotation.NonNull;

import android.util.Log;

import ru.laushkina.rates.BuildConfig;

public class RatesLog {

    public static void d(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable);
        }
    }
}
