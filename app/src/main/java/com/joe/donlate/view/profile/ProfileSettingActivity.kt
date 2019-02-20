package com.joe.donlate.view.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityProfileSettingBinding
import com.joe.donlate.util.*
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.view.meetings_main.MeetingsActivity
import com.joe.donlate.viewmodel.profile.ProfileSettingViewModel
import com.joe.donlate.viewmodel.profile.ProfileSettingViewModelFactory
import org.koin.android.ext.android.inject

class ProfileSettingActivity : BaseActivity<ActivityProfileSettingBinding>() {
    override val layoutResource: Int = R.layout.activity_profile_setting
    private val viewModel: ProfileSettingViewModel by lazy {
        val viewModelFactory: ProfileSettingViewModelFactory by inject("profileSettingViewModelFactory")
        ViewModelProviders.of(this, viewModelFactory).get(ProfileSettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userObserve()
        nameObserve()
        errorObserve()
        startMeetingsClickObserve()
        imageClickObserve()
        updateNameClickObserve()
        startMeetingsActivityObserve()

        getMyAccount()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }


    private fun startMeetingsClickObserve() {
        viewModel.startMeetingsClick.observe(this, Observer {
            checkAccount(UuidUtil.getUuid(this))
        })
    }

    private fun imageClickObserve() {
        viewModel.imageClick.observe(this, Observer {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_IMAGE_CODE)
        })
    }

    private fun updateNameClickObserve() {
        viewModel.updateNameClick.observe(this, Observer {
            val name = viewDataBinding.nameEdit.text.toString()
            if (nameValidate(name)) {
                viewModel.updateName(UuidUtil.getUuid(this), name)
            } else {
                viewModel.setClickable(true)
            }
        })
    }

    private fun nameValidate(name: String): Boolean {
        viewModel.name.value?.let {
            if (it == name) {
                showToast("변경사항이 없습니다.")
                return false
            }
        }
        return when {
            name == "" -> {
                showToast("이름이 공백입니다.")
                false
            }

            name.length !in 2..6 -> {
                showToast("2자에서 6자 사이로 입력해주세요.")
                false
            }
            else -> true
        }
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer {
            viewModel.setClickable(true)
            showToast(it)
        })
    }

    private fun userObserve() {
        viewModel.user.observe(this, Observer {
            viewDataBinding.nameEdit.setText(it["name"].toString())
            GlideUtil.loadFirebaseStorage(this, viewDataBinding.profileImage)
        })
    }


    private fun nameObserve() {
        viewModel.updateName.observe(this, Observer {
            viewModel.setClickable(true)
            showToast(UPDATE_MESSAGE)
        })
    }

    private fun startMeetingsActivityObserve() {
        viewModel.startMeetingsActivity.observe(this, Observer {
            startActivity(Intent(this, MeetingsActivity::class.java))
            finish()
        })
    }

    private fun getMyAccount() {
        viewModel.getMyAccount(UuidUtil.getUuid(this))
    }

    private fun checkAccount(uuid: String) {
        viewModel.checkAccount(uuid)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { _ ->
                val bitmap = BitmapUtil.resize(this, data.data!!, 4)
                bitmap?.let {
                    viewModel.setProgress(true)
                    viewModel.updateImage(UuidUtil.getUuid(this), it)
                } ?: showToast("이미지를 불러오지 못했습니다.")
            } ?: showToast("이미지를 불러오지 못했습니다.")
        }
    }

    /*private fun getPhoneNumber() {
        Utils.getPhoneNumber(this, { phone ->
            viewModel.checkAccount(phone, viewDataBinding.nameEdit.text.toString())
        }, {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_PHONE_STATE },
                REQUEST_PHONE_STATE_CODE
            )
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_STATE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.getPhoneNumber(this, { phone ->
                    viewModel.checkAccount(phone, viewDataBinding.nameEdit.text.toString())
                }, {})
            } else {
                toast(this, "권한체크 해주삼ㅠ")
            }
        }
    }*/
}