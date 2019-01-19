package com.joe.donlate.view_model.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.SplashRepository

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val repository: SplashRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }
}