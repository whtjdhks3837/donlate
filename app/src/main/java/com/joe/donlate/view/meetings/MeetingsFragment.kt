package com.joe.donlate.view.meetings

import android.os.Bundle
import android.util.Log
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
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_meetings.*

class MeetingsFragment : BaseFragment<MeetingsActivity, FragmentMeetingsBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = MeetingsFragment()
    }

    override var layoutResource: Int = R.layout.fragment_meetings
    private lateinit var activityViewModel: MeetingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.setOnFragmentKeyBackListener(this)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        activityViewModel.getMeetings(UuidUtil.getUuid(activity))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        viewDataBinding.setLifecycleOwner(viewLifecycleOwner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomsSubscribe()
        startCreateMeetingSubscribe()
    }

    override fun onStart() {
        super.onStart()
        initViews()
        activity.setOnFragmentKeyBackListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
        viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        viewDataBinding.setLifecycleOwner(null)
    }

    private fun initViews() {
        //TODO("데이터 읽어오는 로직 변경")
        list.layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        initProfileImage()
    }

    private fun initProfileImage() {
        GlideUtil.loadFirebaseStorage(activity, viewDataBinding.profileImage)
    }

    private fun roomsSubscribe() {
        activityViewModel.room.observe(viewLifecycleOwner, Observer {
            list.adapter = activityViewModel.meetingsAdapter
            //TODO("adapter item 동작방식 변경")
            if (activityViewModel.meetingsAdapter.items != it)
                activityViewModel.meetingsAdapter.add(it)
        })
    }

    private fun startCreateMeetingSubscribe() {
        activity.viewModel.startCreateMeeting.observe(this, Observer {
            if (it != null)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, CreateMeetingFragment.instance, MeetingsActivity.FragmentTag.CREATE_MEETING)
                    .addToBackStack(null)
                    .commit()
        })
    }

    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate()
    }
}