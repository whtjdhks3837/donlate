package com.joe.donlate.view.meeting_main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingsBinding
import com.joe.donlate.util.toast
import com.joe.donlate.view.OnKeyBackPressedListener
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meetings.MeetingsFragment
import com.joe.donlate.view.search_place.SearchPlaceFragment
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import com.joe.donlate.view_model.meetings.MeetingsViewModelFactory
import org.koin.android.ext.android.inject

class MeetingsActivity : BaseActivity<ActivityMeetingsBinding>() {
    override val layoutResource: Int = R.layout.activity_meetings
    val viewModel: MeetingsViewModel by lazy {
        val viewModelFactory: MeetingsViewModelFactory by inject("meetingsViewModelFactory")
        ViewModelProviders.of(this, viewModelFactory).get(MeetingsViewModel::class.java)
    }
    private var onKeyBackPressedListener: OnKeyBackPressedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, MeetingsFragment.instance, "meetings")
            .addToBackStack(null)
            .commit()

        errorSubscribe()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun errorSubscribe() {
        viewModel.error.observe(this, Observer {
            viewModel.setProgress(false)
            toast(this, it)
        })
    }

    fun startCreateMeetingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, CreateMeetingFragment.instance, "create")
            .addToBackStack(null)
            .commit()
    }

    fun startMeetingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, MeetingsFragment.instance, "meetings")
            .commit()
    }

    fun startSearchPlaceFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, SearchPlaceFragment.instance, "searchPlace")
            .commit()
    }

    override fun onBackPressed() {
        //Todo : 두 번 눌러야 종료되는 것 고칠 것
        supportFragmentManager.findFragmentByTag("create")?.let {
            startMeetingsFragment()
        } ?: super.onBackPressed()
    }
}