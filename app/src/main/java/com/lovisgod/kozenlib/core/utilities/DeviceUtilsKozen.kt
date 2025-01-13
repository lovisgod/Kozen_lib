package com.lovisgod.kozenlib.core.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pos.sdk.accessory.POIGeneralAPI
import java.lang.reflect.Method


object DeviceUtilsKozen {

    const val i2G = "2G"
    const val i3G = "3G"
    const val i4G = "4G"
    const val UNKNOWN = "UNKNOWN"


    // live data value to observe network state
    private val networkConnectionState = MutableLiveData<Boolean>()
    fun getNetworkConnectionState(): LiveData<Boolean> = networkConnectionState


    private fun checkNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        // should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }


    fun isConnectedToInternet(context: Context) = checkNetwork(context).also { networkConnectionState.postValue(it) }

    @SuppressLint("PrivateApi")
    fun getSerialNumber(): String? {
        var serialNumber: String?
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get: Method = c.getMethod("get", String::class.java)
            serialNumber = get.invoke(c, "gsm.sn1").toString()
            if (serialNumber == "") serialNumber = get.invoke(c, "ril.serialnumber").toString()
            if (serialNumber == "") serialNumber = get.invoke(c, "ro.serialno").toString()
            if (serialNumber == "") serialNumber = get.invoke(c, "sys.serialnumber").toString()
            if (serialNumber == "") serialNumber = Build.SERIAL

            // If none of the methods above worked
            if (serialNumber == "") serialNumber = null
        } catch (e: Exception) {
            e.printStackTrace()
            serialNumber = null
        }
        return serialNumber
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("PrivateApi")
    fun getDeviceSerial(): String? {
        var serialNumber: String?
        try {
            serialNumber = Build.getSerial()
            // If none of the methods above worked
            if (serialNumber == "") serialNumber = null
        } catch (e: Exception) {
            e.printStackTrace()
            serialNumber = null
        }
        return serialNumber
    }

    fun getDeviceSerialKozen(): String? {
        var serialNumber: String?
        try {
            serialNumber = POIGeneralAPI.getDefault().getVersion(POIGeneralAPI.VERSION_TYPE_DSN)
            // If none of the methods above worked
            if (serialNumber == "") serialNumber = null
        } catch (e: Exception) {
            e.printStackTrace()
            serialNumber = null
        }
        return serialNumber
    }

    fun checkSelfPermissionCompat(permission: String, context: Context) =
        ActivityCompat.checkSelfPermission(context, permission)

    fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

    fun requestPermissionsCompat(permissionsArray: Array<String>,
                                                   requestCode: Int, context: Activity) {
        ActivityCompat.requestPermissions(context, permissionsArray, requestCode)
    }

    fun hasPhysicalKeypad(): Boolean {
        val name = android.os.Build.MODEL
        println("device model is $name")

        return (name.contains("P13")
                || name.contains("P3")
                || name.contains("B1796"))
    }
}
