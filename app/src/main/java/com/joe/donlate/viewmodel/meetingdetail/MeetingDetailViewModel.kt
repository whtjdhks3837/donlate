package com.joe.donlate.viewmodel.meetingdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.data.Meeting
import com.joe.donlate.model.MeetingDetailRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.SingleLiveData
import com.joe.donlate.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingDetailViewModel(private val repository: MeetingDetailRepository) : BaseViewModel() {
    private val _meeting = SingleLiveData<Meeting>()
    val meeting: LiveData<Meeting> = _meeting

    fun loadMeetingDetail(url: String) {
        setProgress(true)
        compositeDisposable.add(
            repository.loadMeetingDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .doOnError { setProgress(false) }
                .subscribe({
                    _meeting.value = it.documents.first().toObject(Meeting::class.java)
                }, {
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }
}

class MeetingDetailViewModelFactory(private val repository: MeetingDetailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeetingDetailViewModel(repository) as T
    }
}