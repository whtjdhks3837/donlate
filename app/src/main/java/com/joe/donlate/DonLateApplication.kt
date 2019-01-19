package com.joe.donlate

import android.app.Application
import com.joe.donlate.di.appModule
import org.koin.android.ext.android.startKoin

class DonLateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModule)
    }
}