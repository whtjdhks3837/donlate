package com.joe.donlate.di

import com.joe.donlate.api.NaverMapService
import com.joe.donlate.api.RetrofitService
import com.joe.donlate.model.*
import com.joe.donlate.util.NAVER_API_URL
import com.joe.donlate.view_model.meetings.MeetingsViewModelFactory
import com.joe.donlate.view_model.profile.ProfileSettingViewModelFactory
import com.joe.donlate.view_model.splash.SplashViewModelFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val url = "testurl"
val apiModule = module {
    single("api") {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get())
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(RetrofitService::class.java)
    }

    single("naverMapApi") {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get())
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NAVER_API_URL)
            .build()
            .create(NaverMapService::class.java)
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        } as Interceptor
    }
}

val splashModule = module {
    factory("splashRepository") {
        SplashRepositoryImpl() as SplashRepository
    }
    factory("splashViewModelFactory") {
        SplashViewModelFactory(get("splashRepository"))
    }
}

val profileSettingModule = module {
    factory("profileSettingModule") {
        ProfileSettingRepositoryImpl() as ProfileSettingRepository
    }

    factory("profileSettingViewModelFactory") {
        ProfileSettingViewModelFactory(get("profileSettingModule"))
    }
}

val meetingsModule = module {
    factory("meetingsRepository") {
        MeetingsRepositoryImpl(get("naverMapApi")) as MeetingsRepository
    }

    factory("meetingsViewModelFactory") {
        MeetingsViewModelFactory(get("meetingsRepository"))
    }
}

val appModule = listOf(apiModule, splashModule, profileSettingModule, meetingsModule)