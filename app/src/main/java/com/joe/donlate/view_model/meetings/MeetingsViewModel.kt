package com.joe.donlate.view_model.meetings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.MeetingsRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingsViewModel(private val repository: MeetingsRepository) : BaseViewModel() {

    fun getMeetings(uuid: String) {
        addDisposable(repository.getMeetings(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                it.printStackTrace()
                _error.value = SERVER_ERROR_MESSAGE
            }))
    }
}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val repository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(repository) as T
}