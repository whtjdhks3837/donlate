package com.joe.donlate.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object UuidUtil {
    @SuppressLint("HardwareIds")
    fun getUuid(context: Context) =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)!!
}