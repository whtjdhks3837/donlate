package com.joe.donlate.view.meeting_main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingsBinding
import com.joe.donlate.util.showToast
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meetings.MeetingsFragment
import com.joe.donlate.view.search_place.SearchPlaceFragment
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import com.joe.donlate.view_model.meetings.MeetingsViewModelFactory
import org.koin.android.ext.android.inject

class MeetingsActivity : BaseActivity<ActivityMeetingsBinding>() {
    object FragmentTag {
        const val MEETINGS = "meetings"
        const val CREATE_MEETING = "create_meeting"
        const val SEARCH_PLACE = "search_place"
    }

    override val layoutResource: Int = R.layout.activity_meetings
    val viewModel: MeetingsViewModel by lazy {
        val viewModelFactory: MeetingsViewModelFactory by inject("meetingsViewModelFactory")
        ViewModelProviders.of(this, viewModelFactory).get(MeetingsViewModel::class.java)
    }
    private var onFragmentKeyBackListener: OnFragmentKeyBackListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, MeetingsFragment.instance, FragmentTag.MEETINGS)
            .addToBackStack(FragmentTag.MEETINGS)
            .commit()

        errorSubscribe()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun errorSubscribe() {
        viewModel.error.observe(this, Observer {
            viewModel.setProgress(false)
            showToast(it)
        })
    }

    fun setOnFragmentKeyBackListener(onFragmentKeyBackListener: OnFragmentKeyBackListener?) {
        this.onFragmentKeyBackListener = onFragmentKeyBackListener
    }

    fun fragmentReplace(fragment: Fragment, tag: String) {

    }

    override fun onBackPressed() {
        /*supportFragmentManager.fragments.find { it.isVisible && it.tag == FragmentTag.MEETINGS }
            ?.let { finish() }*/
        supportFragmentManager.fragments.find { it.isVisible }?.let {
            onFragmentKeyBackListener?.onBack(it.tag) ?: super.onBackPressed()
        }
        Log.e("tag", "${supportFragmentManager.backStackEntryCount}")
    }
}