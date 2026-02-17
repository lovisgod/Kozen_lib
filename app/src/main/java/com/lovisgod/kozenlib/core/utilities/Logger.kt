package com.lovisgod.kozenlib.core.utilities

import com.lovisgod.kozenlib.BuildConfig

object Logger {
    private const val TAG = "Kozen_Bundle_Logger"

    fun log(message: String, level: Int = android.util.Log.INFO,  throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            when (level) {
                android.util.Log.DEBUG -> android.util.Log.d(TAG, message, throwable)
                android.util.Log.ERROR -> android.util.Log.e(TAG, message, throwable)
                android.util.Log.INFO -> android.util.Log.i(TAG, message, throwable)
                android.util.Log.WARN -> android.util.Log.w(TAG, message, throwable)
                else -> android.util.Log.v(TAG, message, throwable)
            }
        }
    }
}