package com.joe.donlate.view_model.meetings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.model.value.ReferenceValue
import com.joe.donlate.data.Room
import com.joe.donlate.model.MeetingsRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingsViewModel(private val repository: MeetingsRepository) : BaseViewModel() {
    private val _rooms = MutableLiveData<List<Room>>()
    val room: LiveData<List<Room>> = _rooms
    val listAdapter = MeetingsAdapter()

    fun getMeetings(uuid: String) {
        addDisposable(repository.getMeetings(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isEmpty) {
                    val rooms = it.toObjects(Room::class.java)
                    _rooms.value = rooms
                } else {
                    _error.value = "방이 없어용 ㅠ"
                }
            }, {
                it.printStackTrace()
                _error.value = SERVER_ERROR_MESSAGE
            })
        )
    }
}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val repository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(repository) as T
}