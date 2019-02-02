package com.joe.donlate.view.meetings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
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
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        activity.setOnFragmentKeyBackListener(this)
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
        activity.setOnFragmentKeyBackListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun initViews() {
        list.apply {
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
            adapter = activityViewModel.meetingsAdapter
        }
        initProfileImage()
    }

    private fun getMeetings() {
        Log.e("tag", "getMeetings")
        activityViewModel.getMeetings(UuidUtil.getUuid(activity))
    }

    private fun initProfileImage() {
        GlideUtil.loadFirebaseStorage(activity, viewDataBinding.profileImage)
    }

    private fun roomsSubscribe() {
        activityViewModel.room.observe(this, Observer {
            Log.e("tag", "roomsSubscribe")
            activityViewModel.meetingsAdapter.preAdd(it)
        })
    }

    private fun startCreateMeetingSubscribe() {
        activityViewModel.startCreateMeeting.observe(this, Observer {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, CreateMeetingFragment.instance, MeetingsActivity.FragmentTag.CREATE_MEETING)
                .addToBackStack(null)
                .commit()
        })
    }

    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate()
        /*stackName?.let {
            activity.supportFragmentManager.popBackStackImmediate(it, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } ?: activity.supportFragmentManager.popBackStackImmediate()*/
    }
}