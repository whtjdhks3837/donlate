package com.joe.donlate.di

import com.joe.donlate.model.SplashRepository
import com.joe.donlate.model.SplashRepositoryImpl
import com.joe.donlate.view_model.splash.SplashViewModel
import com.joe.donlate.view_model.splash.SplashViewModelFactory
import org.koin.dsl.module.module

val splashModule = module {
    factory {
        SplashRepositoryImpl() as SplashRepository
    }
    factory {
        SplashViewModelFactory(get())
    }
}