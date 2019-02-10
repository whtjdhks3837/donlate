package com.joe.donlate.view.meetings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.joe.donlate.R
import com.joe.donlate.databinding.FragmentMeetingsBinding
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view.profile.ProfileSettingActivity
import com.joe.donlate.view_model.meetings.MeetingsInput
import com.joe.donlate.view_model.meetings.MeetingsOutput
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_meetings.*

class MeetingsFragment : BaseFragment<MeetingsActivity, FragmentMeetingsBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = MeetingsFragment()
    }

    private val meetingsAdapter = MeetingsAdapter(
        { //touch
            Log.e("tag", "touch")
        },
        { view ->
            activityViewModel.meetingsInput.meetingLongClick()
        },
        { //addClick
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, CreateMeetingFragment.instance, MeetingsActivity.FragmentTag.CREATE_MEETING)
                .addToBackStack(MeetingsActivity.FragmentTag.CREATE_MEETING)
                .commit()
        })
    override var layoutResource: Int = R.layout.fragment_meetings
    private lateinit var activityViewModel: MeetingsViewModel
    private lateinit var activityViewModelOutput: MeetingsOutput
    private lateinit var activityViewModelInput: MeetingsInput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.setOnFragmentKeyBackListener(this)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        activityViewModelOutput = activityViewModel.meetingsOutput
        activityViewModelInput = activityViewModel.meetingsInput
        activityViewModel.getMeetings(UuidUtil.getUuid(activity))
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
        initViews()
        setProfileClickListener()
        meetingsSubscribe()
        meetingLongClickSubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun initViews() {
        list.layoutManager = LinearLayoutManager(activity)
    }

    private fun setProfileClickListener() {
        profile.setOnClickListener {
            startActivity(Intent(activity, ProfileSettingActivity::class.java))
        }
    }

    private fun meetingsSubscribe() {
        activityViewModelOutput.meetings.observe(this, Observer {
            list.adapter = meetingsAdapter
            meetingsAdapter.set(it)
        })
    }

    private fun meetingLongClickSubscribe() {
        activityViewModelOutput.meetingLongClick.observe(this, Observer {
            Log.e("tag", "meetingLongClickSubscribe")
            val params = list.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = deleteModelGuideline.id
            params.endToEnd = root.id
            list.requestLayout()
        })
    }

    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}