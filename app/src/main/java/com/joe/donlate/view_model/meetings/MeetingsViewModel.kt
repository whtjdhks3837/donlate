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
import com.joe.donlate.util.SEARCH_NOT_FOUND
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.SingleLiveData
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view.search_place.list.AddressesAdapter
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MeetingsViewModel(private val repository: MeetingsRepository) : BaseViewModel() {
    private val _rooms = MutableLiveData<LinkedList<Room>>()
    private val _startCreateMeeting = SingleLiveData<Any>()
    private val _startSearchPlaceClick = SingleLiveData<Any>()
    private val _searchPlaceClick = SingleLiveData<Any>()
    private val _place = MutableLiveData<String>()
    private val _createMeetingClick = SingleLiveData<Any>()
    private val _createMeeting = SingleLiveData<Room>()
    private val _searchPlaceResult = SingleLiveData<List<Address>>()

    val room: LiveData<LinkedList<Room>> = _rooms
    val startCreateMeeting: LiveData<Any> = _startCreateMeeting
    val startSearchPlaceClick: LiveData<Any> = _startSearchPlaceClick
    val searchPlaceClick: LiveData<Any> = _searchPlaceClick
    val place: LiveData<String> = _place
    val createMeetingClick: LiveData<Any> = _createMeetingClick
    val createMeeting: LiveData<Room> = _createMeeting
    val searchPlaceResult: LiveData<List<Address>> = _searchPlaceResult

    val title = MutableLiveData<String>()
    val year = MutableLiveData<String>()
    val month = MutableLiveData<String>()
    val day = MutableLiveData<String>()
    val hour = MutableLiveData<String>()
    val min = MutableLiveData<String>()
    val maxParticipants = MutableLiveData<String>()
    val penaltyTime = MutableLiveData<String>()
    val penaltyFee = MutableLiveData<String>()

    val meetingsAdapter = MeetingsAdapter(_startCreateMeeting)
    val addressesAdapter = AddressesAdapter(_place)

    fun getMeetings(uuid: String) {
        setProgress(true)
        addDisposable(
            repository.getMeetings(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { setProgress(false) }
                .subscribe({
                    if (!it.isEmpty) {
                        val rooms = it.toObjects(Room::class.java)
                        val linkedRooms = LinkedList<Room>()
                        linkedRooms.addAll(rooms)
                        _rooms.value = linkedRooms
                    } else {
                        error("방이 없어용 ㅠ")
                    }
                }, {
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
                            //Todo single event live data 필요
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
                    //TODO : 프래그먼트 이동 시 리스트 삭제
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
        _searchPlaceClick.call()
    }

    fun onCreateMeetingClick(view: View) {
        _createMeetingClick.call()
    }

    fun onStartSearchPlaceClick(view: View) {
        _startSearchPlaceClick.call()
    }

    fun initCreateMeetingLiveData() {
        _place.value = null
    }

    fun addRoom(room: Room) {
        _rooms.value?.addFirst(room)
    }

    fun removeMeetingLiveDatas() {
        Log.e("tag", "removeMeetingLiveDatas")
        title.value = null
        year.value = null
        month.value = null
        day.value = null
        hour.value = null
        min.value = null
        maxParticipants.value = null
        penaltyTime.value = null
        penaltyFee.value = null
    }
}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val repository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(repository) as T
}