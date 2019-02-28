package com.joe.donlate.viewmodel.meetingdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.MeetingDetailRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingDetailViewModel(private val repository: MeetingDetailRepository) : BaseViewModel() {
    fun loadMeetingDetail(url: String) {
        compositeDisposable.add(
            repository.loadMeetingDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.documents
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