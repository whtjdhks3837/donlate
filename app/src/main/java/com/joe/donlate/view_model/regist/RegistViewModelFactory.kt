package com.joe.donlate.view_model.regist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.RegistRepository

@Suppress("UNCHECKED_CAST")
class RegistViewModelFactory(private val repository: RegistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            RegistViewModel(repository) as T
}