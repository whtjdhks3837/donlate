package com.joe.donlate.view.splash

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivitySplashBinding
import com.joe.donlate.util.Utils
import com.joe.donlate.util.toast
import com.joe.donlate.view.BaseActivity
import com.joe.donlate.view.regist.RegistActivity
import com.joe.donlate.view_model.splash.SplashViewModel
import com.joe.donlate.view_model.splash.SplashViewModelFactory
import org.koin.android.ext.android.get

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutResource: Int = R.layout.activity_splash
    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, SplashViewModelFactory(get()))
            .get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userObserve()
        userNotFoundObserve()
        errorObserve()
        getMyAccount(Utils.Uuid.getUuid(this))

        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun getMyAccount(uuid: String) {
        viewModel.setProgress(true)
        viewModel.getMyAccount("$uuid@donlate.com")
    }

    private fun userObserve() {
        viewModel.user.observe(this, Observer {
            viewModel.setProgress(false)
        })
    }

    private fun userNotFoundObserve() {
        viewModel.userNotFound.observe(this, Observer {
            viewModel.setProgress(false)
            startActivity(Intent(this, RegistActivity::class.java))
            finish()
        })
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer { msg ->
            viewModel.setProgress(false)
            toast(this, msg)
        })
    }
}
