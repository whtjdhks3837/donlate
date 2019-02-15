package com.joe.donlate.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivitySplashBinding
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.util.showToast
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view.profile.ProfileSettingActivity
import com.joe.donlate.view_model.splash.SplashViewModel
import com.joe.donlate.view_model.splash.SplashViewModelFactory
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val layoutResource: Int = R.layout.activity_splash
    private val viewModel: SplashViewModel by lazy {
        val viewModelFactory: SplashViewModelFactory by inject("splashViewModelFactory")
        ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userObserve()
        userNotFoundObserve()
        errorObserve()
        viewModel.getMyAccount(UuidUtil.getUuid(this))
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun userObserve() {
        viewModel.user.observe(this, Observer {
            startActivity(Intent(this, MeetingsActivity::class.java))
            finish()
        })
    }

    private fun userNotFoundObserve() {
        viewModel.userNotFound.observe(this, Observer {
            startActivity(Intent(this, ProfileSettingActivity::class.java))
            finish()
        })
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer { msg ->
            showToast(msg)
        })
    }
}
