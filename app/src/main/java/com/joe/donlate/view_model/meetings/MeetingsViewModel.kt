package com.joe.donlate.view_model.meetings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentChange
import com.joe.donlate.data.Address
import com.joe.donlate.data.Meeting
import com.joe.donlate.data.MeetingItemMode
import com.joe.donlate.data.MeetingItemNormalMode
import com.joe.donlate.model.MeetingsRepository
import com.joe.donlate.util.CREATE_FAILURE_MESSAGE
import com.joe.donlate.util.SEARCH_NOT_FOUND
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.SingleLiveData
import com.joe.donlate.view_model.BaseViewModel
import com.joe.donlate.view_model.CLICK
import com.joe.donlate.view_model.Input
import com.joe.donlate.view_model.Output
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

interface MeetingsInput : Input {
    fun meetingClick()
    fun meetingLongClick()
}

interface MeetingsOutput : Output {
    val meetings: LiveData<List<Meeting?>>
    val meetingLongClick: LiveData<Boolean>
    val meetingMode: LiveData<MeetingItemMode>
}

interface CreateMeetingInput : Input {
    fun startSearchPlaceFragmentClick()
    fun createMeetingClick()
}

interface CreateMeetingOutput : Output {
    val createMeeting: LiveData<Meeting>
    val startSearchPlaceFragment: LiveData<Any>
    val startMeetingsFragment: LiveData<Any>
    val place: LiveData<Address?>
    val title: MutableLiveData<String>
    val year: MutableLiveData<String>
    val month: MutableLiveData<String>
    val day: MutableLiveData<String>
    val hour: MutableLiveData<String>
    val min: MutableLiveData<String>
    val maxParticipants: MutableLiveData<String>
    val penaltyTime: MutableLiveData<String>
    val penaltyFee: MutableLiveData<String>
}

interface SearchPlaceInput : Input {
    fun searchPlaceClick()
    fun startCreateMeetingFragmentClick()
}

interface SearchPlaceOutput : Output {
    val startCreateMeetingFragment: LiveData<Any>
    val place: LiveData<Address?>
    val searchPlace: LiveData<Any>
    val searchPlaceResult: LiveData<List<Address>>
}

class MeetingsViewModel(private val repository: MeetingsRepository) : BaseViewModel() {
    private val _meetings = MutableLiveData<List<Meeting?>>()
    private val _meetingLongClick = SingleLiveData<Boolean>()
    private val _meetingMode = SingleLiveData<MeetingItemMode>()

    private val _createMeeting = SingleLiveData<Meeting>()
    private val _startSearchPlaceFragment = SingleLiveData<Any>()
    private val _startMeetingsFragment = SingleLiveData<Any>()
    private val _place = MutableLiveData<Address?>()

    private val _searchPlaceResult = SingleLiveData<List<Address>>()
    private val _startCreateMeetingFragment = SingleLiveData<Any>()
    private val _searchPlace = SingleLiveData<Any>()

    private val startSearchPlaceFragmentClick = PublishSubject.create<Any>()
    private val createMeetingClick = PublishSubject.create<Any>()
    private val startCreateMeetingFragmentClick = PublishSubject.create<Any>()
    private val searchPlaceClick = PublishSubject.create<Any>()

    val meetingsInput = object : MeetingsInput {
        override fun meetingClick() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun meetingLongClick() {
            _meetingLongClick.value = true
        }
    }

    val meetingsOutput = object : MeetingsOutput {
        override val meetings: LiveData<List<Meeting?>> = _meetings
        override val meetingLongClick: LiveData<Boolean> = _meetingLongClick
        override val meetingMode: LiveData<MeetingItemMode> = _meetingMode
    }

    val createMeetingInput = object : CreateMeetingInput {
        override fun startSearchPlaceFragmentClick() = startSearchPlaceFragmentClick.onNext(CLICK)
        override fun createMeetingClick() = createMeetingClick.onNext(CLICK)
    }

    val createMeetingOutput = object : CreateMeetingOutput {
        override val startSearchPlaceFragment: LiveData<Any> = _startSearchPlaceFragment
        override val startMeetingsFragment: LiveData<Any> = _startMeetingsFragment
        override val createMeeting: LiveData<Meeting> = _createMeeting
        override val place: LiveData<Address?> = _place
        override val title: MutableLiveData<String> = MutableLiveData()
        override val year: MutableLiveData<String> = MutableLiveData()
        override val month: MutableLiveData<String> = MutableLiveData()
        override val day: MutableLiveData<String> = MutableLiveData()
        override val hour: MutableLiveData<String> = MutableLiveData()
        override val min: MutableLiveData<String> = MutableLiveData()
        override val maxParticipants: MutableLiveData<String> = MutableLiveData()
        override val penaltyTime: MutableLiveData<String> = MutableLiveData()
        override val penaltyFee: MutableLiveData<String> = MutableLiveData()
    }

    val searchPlaceInput = object : SearchPlaceInput {
        override fun searchPlaceClick() = searchPlaceClick.onNext(CLICK)
        override fun startCreateMeetingFragmentClick() = startCreateMeetingFragmentClick.onNext(CLICK)
    }

    val searchPlaceOutput = object : SearchPlaceOutput {
        override val startCreateMeetingFragment: LiveData<Any> = _startCreateMeetingFragment
        override val place: LiveData<Address?> = _place
        override val searchPlace: LiveData<Any> = _searchPlace
        override val searchPlaceResult: LiveData<List<Address>> = _searchPlaceResult
    }

    private val createMeetingOutputSet =
        setOf(createMeetingOutput.title, createMeetingOutput.year, createMeetingOutput.month, createMeetingOutput.day, createMeetingOutput.hour,
            createMeetingOutput.min, createMeetingOutput.maxParticipants, createMeetingOutput.penaltyTime, createMeetingOutput.penaltyFee)

    init {
        _meetingMode.value = MeetingItemNormalMode
        compositeDisposable.addAll(
            startSearchPlaceFragmentClick.subscribe { _startSearchPlaceFragment.call() },
            createMeetingClick.subscribe { _createMeeting.call() },
            startCreateMeetingFragmentClick.subscribe { _startCreateMeetingFragment.call() },
            searchPlaceClick.subscribe { _searchPlace.call() }
        )
    }

    fun getMeetings(uuid: String) {
        setProgress(true)
        compositeDisposable.add(
            repository.getMeetings(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .subscribe({
                    it.query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        querySnapshot?.let { snapshot ->
                            snapshot.documents.forEach {
                                it.data?.forEach { t, u ->
                                    Log.e("tag", "$t $u")
                                }
                            }
                            _meetings.value = snapshot.documents.map { it.toObject(Meeting::class.java) }
                        }

                        firebaseFirestoreException?.let { exception ->
                            exception.printStackTrace()
                            error("firebaseFirestoreException")
                        }
                    }
                }, {
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    fun leaveMeetings(url: String, uuid: String) {
        setProgress(true)
        compositeDisposable.add(
            repository.leaveMeeting(url, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .doOnError { setProgress(false) }
                .subscribe({

                }, {
                    it.printStackTrace()
                    error("삭제에 실패했습니다.")
                })
        )
    }

    fun createMeeting(uuid: String, meeting: Meeting) {
        setProgress(true)
        compositeDisposable.add(
            repository.createMeeting(uuid, meeting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .subscribe({
                    /*it.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        documentSnapshot?.let { snapshot ->
                            _meetings.value?.addFirst(snapshot.toObject(Meeting::class.java))
                            _startMeetingsFragment.call()
                        } ?: error(CREATE_FAILURE_MESSAGE)
                        firebaseFirestoreException?.let { exception ->
                            exception.printStackTrace()
                            error(SERVER_ERROR_MESSAGE)
                        }
                    }*/
                }, {
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    fun searchPlace(query: String) {
        setProgress(true)
        compositeDisposable.add(repository.getAddress(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setProgress(false) }
            .subscribe({
                if (!it.addresses.isEmpty()) {
                    _searchPlaceResult.value = it.addresses
                } else {
                    error(SEARCH_NOT_FOUND)
                }
            }, {
                it.printStackTrace()
                error(SERVER_ERROR_MESSAGE)
            })
        )
    }

    fun setPlace(address: Address?) {
        _place.value = address
    }

    fun initCreateMeetingBindingData() {
        createMeetingOutputSet.forEach { it.value = "" }
        _place.value = null
    }

    fun setMeetingMode(mode: MeetingItemMode) {
        _meetingMode.value = mode
    }
}

@Suppress("UNCHECKED_CAST")
class MeetingsViewModelFactory(private val repository: MeetingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MeetingsViewModel(repository) as T
}