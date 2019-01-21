package com.joe.donlate.di

import com.joe.donlate.model.RegistRepository
import com.joe.donlate.model.RegistRepositoryImpl
import com.joe.donlate.view_model.profile.RegistViewModelFactory
import org.koin.dsl.module.module

val registModule = module {
    factory {
        RegistRepositoryImpl() as RegistRepository
    }

    factory {
        RegistViewModelFactory(get())
    }
}