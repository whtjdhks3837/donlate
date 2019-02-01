package com.joe.donlate.view.create_meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.model.ResourcePath
import com.joe.donlate.R
import com.joe.donlate.data.Room
import com.joe.donlate.databinding.FragmentCreateMeetingBinding
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view_model.meetings.MeetingsViewModel

class CreateMeetingFragment : BaseFragment<MeetingsActivity, FragmentCreateMeetingBinding>() {
    companion object {
        val instance = CreateMeetingFragment()
    }
    override var layoutResource: Int = R.layout.fragment_create_meeting

    private lateinit var activityViewModel: MeetingsViewModel

    //Todo : 저장 성공 시 data 약속 프래그먼트로 넘길 것
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        createMeetingSubscribe()
        createMeetingClickSubscribe()
        placeSubscribe()
        startSearchPlaceClickSubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        return view
    }

    private fun createMeetingSubscribe() {
        activityViewModel.createMeeting.observe(this, Observer {
            activityViewModel.meetingsAdapter.add(it, activity.viewModel.meetingsAdapter.itemCount - 1)
            activity.startMeetingsFragment()
        })
    }

    private fun placeSubscribe() {
        activityViewModel.place.observe(this, Observer {
            viewDataBinding.searchPlace.setText(it)
        })
    }

    private fun createMeetingClickSubscribe() {
        activityViewModel.createMeetingClick.observe(this, Observer {
            activityViewModel.createMeeting(UuidUtil.getUuid(activity),
                Room(title = "12341", maxParticipants = 10, url = "testurl3", penaltyTime = 20, penaltyFee = 500, participants = listOf(
                    DocumentReference.forPath(ResourcePath.fromString("/users/${UuidUtil.getUuid(activity)}"), firebaseDatabase))))
        })
    }

    private fun startSearchPlaceClickSubscribe() {
        activityViewModel.startSearchPlaceClick.observe(this, Observer {
            activity.startSearchPlaceFragment()
        })
    }
}