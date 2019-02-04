package com.joe.donlate.view_model.meetings

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.data.Address
import com.joe.donlate.data.Room
import com.joe.donlate.model.MeetingsRepository
import com.joe.donlate.util.CREATE_FAILURE_MESSAGE
import com.joe.donlate.util.ClickLiveData
import com.joe.donlate.util.SEARCH_NOT_FOUND
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view.search_place.list.AddressesAdapter
import com.joe.donlate.view_model.BaseViewModel
import com.joe.donlate.view_model.CLICK
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingsViewModel(private val repository: MeetingsRepository) : BaseViewModel() {
    private val _rooms = MutableLiveData<List<Room>>()
    private val _startCreateMeeting = MutableLiveData<Any>()
    private val _startSearchPlaceClick = MutableLiveData<Any>()
    private val _searchPlaceClick = MutableLiveData<Any>()
    private val _place = MutableLiveData<String>()
    private val _createMeetingClick = ClickLiveData<Any>()
    private val _createMeeting = MutableLiveData<Room>()
    private val _searchPlaceResult = MutableLiveData<List<Address>>()

    val room: LiveData<List<Room>> = _rooms
    val startCreateMeeting: LiveData<Any> = _startCreateMeeting
    val startSearchPlaceClick: LiveData<Any> = _startSearchPlaceClick
    val searchPlaceClick: LiveData<Any> = _searchPlaceClick
    val place: LiveData<String> = _place
    val createMeetingClick: LiveData<Any> = _createMeetingClick
    val createMeeting: LiveData<Room> = _createMeeting
    val searchPlaceResult: LiveData<List<Address>> = _searchPlaceResult

    val meetingsAdapter = MeetingsAdapter(_startCreateMeeting)
    val addressesAdapter = AddressesAdapter(_place)

    fun getMeetings(uuid: String) {
        Log.e("tag", "getMeetings")
        setProgress(true)
        addDisposable(
            repository.getMeetings(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { setProgress(false) }
                .subscribe({
                    Log.e("tag", "getMeetings onSucceess")
                    if (!it.isEmpty) {
                        Log.e("tag", "not empty")
                        val rooms = it.toObjects(Room::class.java)
                        _rooms.value = rooms
                    } else {
                        error("방이 없어용 ㅠ")
                    }
                }, {
                    Log.e("tag", "getMeetings onFailure")
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    fun createMeeting(uuid: String, meeting: Room) {
        setProgress(true)
        addDisposable(
            repository.createMeeting(uuid, meeting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { setProgress(false) }
                .subscribe({
                    it.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        documentSnapshot?.let { snapshot ->
                            _createMeeting.value = snapshot.toObject(Room::class.java)
                        } ?: error(CREATE_FAILURE_MESSAGE)

                        firebaseFirestoreException?.let { exception ->
                            exception.printStackTrace()
                            error(SERVER_ERROR_MESSAGE)
                        }
                    }
                }, {
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    fun searchPlace(query: String) {
        setProgress(true)
        addDisposable(repository.getAddress(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setProgress(false) }
            .subscribe({
                if (!it.addresses.isEmpty()) {
                    _searchPlaceResult.value = it.addresses
                } else {
                    error(SEARCH_NOT_FOUND)
                }
            }, {
                it.printStackTrace()
                error(SERVER_ERROR_MESSAGE)
            }))
    }

    fun onSearchPlaceClick(view: View) {
        _searchPlaceClick.value = CLICK
    }

    fun onCreateMeetingClick(view: View) {
        _createMeetingClick.value = CLICK
    }

    fun onStartSearchPlaceClick(view: View) {
        _startSearchPlaceClick.value = CLICK
    }
}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val repository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(repository) as T
}