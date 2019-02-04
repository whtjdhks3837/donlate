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
import com.joe.donlate.view.meetings.MeetingsFragment
import com.joe.donlate.view.search_place.SearchPlaceFragment
import com.joe.donlate.view_model.meetings.MeetingsViewModel

class CreateMeetingFragment : BaseFragment<MeetingsActivity, FragmentCreateMeetingBinding>(),
    OnFragmentKeyBackListener {
    companion object {
        val instance = CreateMeetingFragment()
    }

    override var layoutResource: Int = R.layout.fragment_create_meeting
    private lateinit var activityViewModel: MeetingsViewModel

    //Todo : 저장 성공 시 data 약속 프래그먼트로 넘길 것
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        viewDataBinding.setLifecycleOwner(viewLifecycleOwner)
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
        viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        viewDataBinding.viewModel = null
        viewDataBinding.setLifecycleOwner(null)
    }

    private fun createMeetingSubscribe() {
        activityViewModel.createMeeting.observe(viewLifecycleOwner, Observer {
            activityViewModel.meetingsAdapter.addFirst(it)
            activity.supportFragmentManager.popBackStackImmediate()
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

    private fun startSearchPlaceClickSubscribe() {
        activityViewModel.startSearchPlaceClick.observe(viewLifecycleOwner, Observer {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SearchPlaceFragment.instance, MeetingsActivity.FragmentTag.SEARCH_PLACE)
                .addToBackStack(null)
                .commit()
        })
    }

    override fun onBack(stackName: String?) {
        //TODO("pop stack 오류 해결")
        Log.e("tag", "create pop stack!!")
        activity.supportFragmentManager.popBackStack()
    }
}