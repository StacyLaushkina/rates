package ru.laushkina.rates.util

import android.util.Log
import ru.laushkina.rates.BuildConfig

class RatesLog {
    companion object {
        fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        fun e(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message)
            }
        }

        fun e(tag: String, message: String, throwable: Throwable?) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message, throwable)
            }
        }
    }
}