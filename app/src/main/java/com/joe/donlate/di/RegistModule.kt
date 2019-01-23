package com.joe.donlate.di

import com.joe.donlate.model.ProfileSettingRepository
import com.joe.donlate.model.ProfileSettingRepositoryImpl
import com.joe.donlate.view_model.profile.ProfileSettingViewModelFactory
import org.koin.dsl.module.module

val registModule = module {
    factory {
        ProfileSettingRepositoryImpl() as ProfileSettingRepository
    }

    factory {
        ProfileSettingViewModelFactory(get())
    }
}