package com.joe.donlate.view.meetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joe.donlate.R
import com.joe.donlate.databinding.FragmentMeetingsBinding
import com.joe.donlate.util.GlideUtil
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_meetings.*

class MeetingsFragment : BaseFragment<MeetingsActivity, FragmentMeetingsBinding>() {
    companion object {
        val instance = MeetingsFragment()
    }
    override var layoutResource: Int = R.layout.fragment_meetings
    private lateinit var activityViewModel: MeetingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        setRoomAddButton()
        getMeetings()
        roomsSubscribe()
        startCreateMeetingSubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        return view
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun setRoomAddButton() = activityViewModel.listAdapter.setAddButton()

    private fun initViews() {
        list.apply {
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
            adapter = activityViewModel.listAdapter
        }
        initProfileImage()
    }

    private fun getMeetings() {
        activityViewModel.getMeetings(UuidUtil.getUuid(activity))
    }

    private fun initProfileImage() {
        GlideUtil.loadFirebaseStorage(activity, viewDataBinding.profileImage)
    }

    private fun roomsSubscribe() {
        activityViewModel.room.observe(this, Observer {
            activityViewModel.listAdapter.preAdd(it)
        })
    }

    private fun startCreateMeetingSubscribe() {
        activityViewModel.startCreateMeeting.observe(this, Observer {
            activity.startCreateMeetingFragment()
        })
    }

}