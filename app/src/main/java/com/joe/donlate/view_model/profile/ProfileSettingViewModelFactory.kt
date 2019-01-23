package com.joe.donlate.view_model.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.ProfileSettingRepository

@Suppress("UNCHECKED_CAST")
class ProfileSettingViewModelFactory(private val repository: ProfileSettingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ProfileSettingViewModel(repository) as T
}