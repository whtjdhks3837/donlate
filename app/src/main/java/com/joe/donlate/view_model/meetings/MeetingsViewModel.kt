package com.joe.donlate.view_model.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.MeetingsRepository
import com.joe.donlate.view_model.BaseViewModel

class MeetingsViewModel(private val meetingsRepository: MeetingsRepository) : BaseViewModel() {

}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val meetingsRepository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(meetingsRepository) as T
}