package com.lovisgod.kozenlib.core.utilities

import android.content.Context
import android.content.SharedPreferences
import com.lovisgod.kozenlib.core.data.utilsData.Constants

object PrefUtils {

    private lateinit var iswPrefs: SharedPreferences

    private fun initialize(context: Context) {
        iswPrefs = context.getSharedPreferences("ng.insterswitch.shared+preference+name", Context.MODE_PRIVATE)
    }

    private fun getString(context: Context, key: String, defaultValue: String? = null): String? {
        initialize(context)
        return iswPrefs.getString(key, defaultValue)
    }

    fun saveString(context: Context, key: String, value: String) {
        initialize(context)
        iswPrefs.edit().putString(key, value).apply()
    }

    fun getContactlessCvmLimit(context: Context): String = getString(context, Constants.KEY_CONTACTLESS_CVM_LIMIT, Constants.CONTACTLESS_CVM_LIMIT.toString())!!
    fun getContactlessFloorLimit(context: Context): String = getString(context, Constants.KEY_CONTACTLESS_FLOOR_LIMIT, Constants.CONTACTLESS_FLOOR_LIMIT.toString())!!
    fun getContactlessTransLimit(context: Context): String = getString(context, Constants.KEY_CONTACTLESS_TRANS_LIMIT, Constants.CONTACTLESS_TRANS_LIMIT.toString())!!
    fun getContactlessTransNoDeviceLimit(context: Context): String = getString(context, Constants.KEY_CONTACTLESS_TRANS_NO_DEVICE_LIMIT, Constants.CONTACTLESS_TRANS_NO_DEVICE_LIMIT.toString())!!
}