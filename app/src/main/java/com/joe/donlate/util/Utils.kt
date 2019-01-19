package com.joe.donlate.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat

object Utils {
    @SuppressLint("HardwareIds")
    fun getPhoneStatePermission(context: Context, grant: (number: String) -> Unit, request: () -> Unit) {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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
}