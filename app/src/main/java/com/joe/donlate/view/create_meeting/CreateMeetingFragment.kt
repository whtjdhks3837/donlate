package com.joe.donlate.view.create_meeting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.model.ResourcePath
import com.joe.donlate.R
import com.joe.donlate.data.Room
import com.joe.donlate.databinding.FragmentCreateMeetingBinding
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view.search_place.SearchPlaceFragment
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_create_meeting.*
import java.util.*

class CreateMeetingFragment : BaseFragment<MeetingsActivity, FragmentCreateMeetingBinding>(),
    OnFragmentKeyBackListener {
    companion object {
        val instance = CreateMeetingFragment()
        private const val MAX_OF_PARTICIPANTS = 20
        private const val MAX_OF_PENALTY_TIME = 10
        private const val MAX_OF_PENALTY_FEE = 5000
    }

    override var layoutResource: Int = R.layout.fragment_create_meeting
    private lateinit var activityViewModel: MeetingsViewModel

    //Todo : 저장 성공 시 data 약속 프래그먼트로 넘길 것
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = activity.run { ViewModelProviders.of(this).get(MeetingsViewModel::class.java) }
        activityViewModel.initCreateMeetingBindingData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        viewDataBinding.setLifecycleOwner(this)
        activity.setOnFragmentKeyBackListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createMeetingSubscribe()
        createMeetingClickSubscribe()
        placeSubscribe()
        startSearchPlaceClickSubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }


    private fun createMeetingSubscribe() {
        activityViewModel.createMeeting.observe(viewLifecycleOwner, Observer {
            activityViewModel.initCreateMeetingLiveData()
            activityViewModel.addRoom(it)
            activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })
    }

    private fun placeSubscribe() {
        //TODO("장소 Live data 삭제")
        activityViewModel.place.observe(viewLifecycleOwner, Observer {
            viewDataBinding.searchPlace.setText(it)
        })
    }

    private fun createMeetingClickSubscribe() {
        activityViewModel.createMeetingClick.observe(viewLifecycleOwner, Observer {
            /*if (CreateValidator().validate()) {
                val room = Room(
                    title = editTitle.text.toString(),
                    createAt = Timestamp.now(),
                    deadLine = Timestamp(Date(1000L)),
                    coordinate = GeoPoint(10.0, 10.0),
                    maxParticipants = maxParticipants.text.toString().toInt(),
                    url = makeUrl(),
                    penaltyTime = penaltyTime.text.toString().toInt(),
                    penaltyFee = penaltyFee.text.toString().toInt(),
                    participants = listOf(DocumentReference.forPath(
                        ResourcePath.fromString("/users/${UuidUtil.getUuid(activity)}"),
                        firebaseDatabase
                    ))
                )
            }*/
            activityViewModel.createMeeting(
                UuidUtil.getUuid(activity),
                Room(
                    title = "12341",
                    maxParticipants = 10,
                    url = "testurl3",
                    penaltyTime = 20,
                    penaltyFee = 500,
                    participants = listOf(
                        DocumentReference.forPath(
                            ResourcePath.fromString("/users/${UuidUtil.getUuid(activity)}"),
                            firebaseDatabase
                        )
                    )
                )
            )
        })
    }

    private fun makeUrl() = ""

    private fun startSearchPlaceClickSubscribe() {
        activityViewModel.startSearchPlaceClick.observe(viewLifecycleOwner, Observer {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SearchPlaceFragment.instance, MeetingsActivity.FragmentTag.SEARCH_PLACE)
                .addToBackStack(MeetingsActivity.FragmentTag.SEARCH_PLACE)
                .commit()
        })
    }

    override fun onBack(stackName: String?) {
        activityViewModel.initCreateMeetingLiveData()
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    inner class CreateValidator {
        fun validate() = when {
            isTitleEmpty() -> {
                false
            }
            isYearLesser() -> {
                false
            }
            isMonthLesser() -> {
                false
            }
            isDayLesser() -> {
                false
            }
            isDateLesser() -> {
                false
            }
            numOfParticipantsValidate() -> {
                false
            }
            penaltyTimeValidate() -> {
                false
            }
            penaltyFeeValidate() -> {
                false
            }
            isPlaceEmpty() -> {
                false
            }
            else -> true
        }

        private fun isTitleEmpty() = editTitle.text?.isEmpty() ?: false

        private fun isYearLesser() = year.text.toString().toInt() < Calendar.YEAR

        private fun isMonthLesser() = month.text.toString().toInt() < Calendar.MONTH

        private fun isDayLesser() = day.text.toString().toInt() < Calendar.DAY_OF_MONTH

        //TODO : 현재날짜랑 비교
        private fun isDateLesser(): Boolean {
            val year = year.text.toString()
            val month = month.text.toString()
            val day = day.text.toString()
            return true
        }

        private fun numOfParticipantsValidate() = maxParticipants.text.toString().toInt() >= MAX_OF_PARTICIPANTS

        private fun penaltyTimeValidate() = penaltyTime.text.toString().toInt() > MAX_OF_PENALTY_TIME

        private fun penaltyFeeValidate() = penaltyTime.text.toString().toInt() > MAX_OF_PENALTY_FEE

        private fun isPlaceEmpty() = searchPlace.text.isEmpty()
    }
}