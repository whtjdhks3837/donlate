package com.joe.donlate.view.meetings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.joe.donlate.R
import com.joe.donlate.data.MeetingItemDeleteMode
import com.joe.donlate.data.MeetingItemNormalMode
import com.joe.donlate.databinding.FragmentMeetingsBinding
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.createmeeting.CreateMeetingFragment
import com.joe.donlate.view.meetingdetail.MeetingDetailActivity
import com.joe.donlate.view.meetings_main.MeetingsActivity
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view.profile.ProfileSettingActivity
import com.joe.donlate.viewmodel.meetings.MeetingsInput
import com.joe.donlate.viewmodel.meetings.MeetingsOutput
import com.joe.donlate.viewmodel.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_meetings.*

class MeetingsFragment : BaseFragment<MeetingsActivity, FragmentMeetingsBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = MeetingsFragment()
    }

    private val meetingsAdapter = MeetingsAdapter(
        {
            val intent = Intent(activity, MeetingDetailActivity::class.java)
            intent.putExtra("meeting", it)
            startActivity(intent)
        },
        { _, position ->
            setAnim(position)
            activityViewModel.setMeetingMode(MeetingItemDeleteMode)
        })
    override var layoutResource: Int = R.layout.fragment_meetings
    private lateinit var activityViewModel: MeetingsViewModel
    private lateinit var activityViewModelOutput: MeetingsOutput
    private lateinit var activityViewModelInput: MeetingsInput

    private fun setAnim(position: Int) {
        meetingsAdapter.setAnimation(position)
    }

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
        meetingsObserve()
        meetingLongClickObserve()

        meetingAdd.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, CreateMeetingFragment.instance, MeetingsActivity.FragmentTag.CREATE_MEETING)
                .addToBackStack(MeetingsActivity.FragmentTag.CREATE_MEETING)
                .commit()
        }

        viewDataBinding.leave.setOnClickListener { _ ->
            meetingsAdapter.items.find { it.mode is MeetingItemDeleteMode }?.let {
                activityViewModel.leaveMeetings(it.url, UuidUtil.getUuid(activity))
            }
        }
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

    private fun meetingsObserve() {
        activityViewModelOutput.meetings.observe(this, Observer { data ->
            if (list.adapter == null)
                list.adapter = meetingsAdapter
            meetingsAdapter.set(data.filterNotNull())
            activityViewModel.setMeetingMode(MeetingItemNormalMode)
        })
    }

    private fun meetingLongClickObserve() {
        activityViewModelOutput.meetingLongClick.observe(this, Observer {
            list.animation = AnimationUtils.loadAnimation(activity, R.anim.meeting_delete_mode_anim)
        })
    }

    override fun onBack(stackName: String?) {
        meetingsAdapter.items.find { it.mode is MeetingItemDeleteMode }?.let {
            meetingsAdapter.initMode()
            activityViewModel.setMeetingMode(MeetingItemNormalMode)
        } ?: activity.finish()
    }
}