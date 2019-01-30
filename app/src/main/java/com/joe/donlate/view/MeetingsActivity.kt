package com.joe.donlate.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingsBinding
import com.joe.donlate.util.toast
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meetings.MeetingsFragment
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

        val transaction = supportFragmentManager
        transaction.beginTransaction()
            .add(R.id.fragment, MeetingsFragment.instance, "meetings")
            .addToBackStack("meetings_stack")
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
        val transaction = supportFragmentManager
        transaction.beginTransaction()
            .replace(R.id.fragment, CreateMeetingFragment.instance, "create")
            .commit()
    }

    fun startMeetingsFragment() {
        val transaction = supportFragmentManager
        transaction.beginTransaction()
            .replace(R.id.fragment, MeetingsFragment.instance, "meetings")
            .commit()
    }

    fun setOnKeyBackPressedListener(onKeyBackPressedListener: OnKeyBackPressedListener?) {
        this.onKeyBackPressedListener = onKeyBackPressedListener
    }

    /*override fun onBackPressed() {
        Log.e("tag", "${supportFragmentManager.backStackEntryCount}")
        supportFragmentManager.findFragmentByTag("create")?.let {
            Log.e("tag", it.tag)
            supportFragmentManager.beginTransaction().remove(it).replace(R.id.fragment, meetingsFragment).commit()
            supportFragmentManager.popBackStack("meetings_stack", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.fragments.forEach {
                Log.e("tag", "${it.tag}???")
            }
        } ?: super.onBackPressed()
    }*/
}