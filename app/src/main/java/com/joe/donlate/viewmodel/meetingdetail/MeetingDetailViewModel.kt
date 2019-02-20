package com.joe.donlate.viewmodel.meetingdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.viewmodel.BaseViewModel

class MeetingDetailViewModel : BaseViewModel() {

}

class MeetingDetailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeetingDetailViewModel() as T
    }
}