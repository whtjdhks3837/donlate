package com.joe.donlate.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat

object Utils {
    @SuppressLint("HardwareIds")
    fun getPhoneNumber(context: Context, grant: (number: String) -> Unit, request: () -> Unit) {
        val manager = getTelephonyManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                grant(manager.line1Number.replace("+82", "0"))
            } else {
                request()
            }
        } else {
            grant(manager.line1Number.replace("+82", "0"))
        }
    }

    private fun getTelephonyManager(context: Context) =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    object Uuid {
        @SuppressLint("HardwareIds")
        fun getUuid(context: Context) =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)!!
    }
}