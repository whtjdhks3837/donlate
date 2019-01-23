package com.joe.donlate.view.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityProfileSettingBinding
import com.joe.donlate.util.*
import com.joe.donlate.view.BaseActivity
import com.joe.donlate.view.splash.SplashActivity
import com.joe.donlate.view_model.profile.ProfileSettingViewModel
import com.joe.donlate.view_model.profile.ProfileSettingViewModelFactory
import org.koin.android.ext.android.get

class ProfileSettingActivity : BaseActivity<ActivityProfileSettingBinding>() {
    override val layoutResource: Int = R.layout.activity_profile_setting
    private val viewModel: ProfileSettingViewModel by lazy {
        ViewModelProviders.of(this, ProfileSettingViewModelFactory(get())).get(ProfileSettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        getMyAccount()
    }

    private fun init() {
        userObserve()
        nicknameObserve()
        errorObserve()
        startMeetingsClickObserve()
        imageClickObserve()
        updateNicknameClickObserve()
        startMettingsActivityObserve()

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

    private fun updateNicknameClickObserve() {
        viewModel.updateNicknameClick.observe(this, Observer {
            val name = viewDataBinding.nameEdit.text.toString()
            if (nicknameValidate(name)) {
                viewModel.setProgress(true)
                viewModel.updateNickname(UuidUtil.getUuid(this), name)
            } else {
                // Todo : 버튼 안눌리는 것 고칠 것
                viewModel.setClickable(true)
            }
        })
    }

    private fun nicknameValidate(name: String): Boolean {
        if (name == "") {
            toast(this, "이름이 공백입니다.")
            return false
        }
        if (name.length !in 2..6) {
            toast(this, "2자에서 6자 사이로 입력해주세요..")
            return false
        }
        return true
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer {
            viewModel.setProgress(false)
            toast(this, it)
        })
    }

    private fun userObserve() {
        viewModel.user.observe(this, Observer {
            viewModel.setProgress(false)
            viewDataBinding.nameEdit.setText(it["name"].toString())
            GlideUtil.loadFirebaseStorage(this, viewDataBinding.profileImage)
        })
    }


    private fun nicknameObserve() {
        viewModel.nickname.observe(this, Observer {
            viewModel.setProgress(false)
            toast(this, UPDATE_MESSAGE)
        })
    }

    private fun startMettingsActivityObserve() {
        viewModel.startMeetingsActivity.observe(this, Observer {
            viewModel.setProgress(false)
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        })
    }

    private fun getMyAccount() {
        viewModel.setProgress(true)
        viewModel.getMyAccount(UuidUtil.getUuid(this))
    }

    private fun checkAccount(uuid: String) {
        viewModel.setProgress(true)
        viewModel.checkAccount(uuid)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { _ ->
                val bitmap = getResizeBitmap(data.data!!)
                bitmap?.let {
                    viewModel.setProgress(true)
                    viewModel.updateImage(UuidUtil.getUuid(this), it)
                } ?: toast(this, "이미지를 불러오지 못했습니다.")
            } ?: toast(this, "이미지를 불러오지 못했습니다.")
        }
    }

    private fun getResizeBitmap(uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        return BitmapFactory.decodeStream(inputStream, null, options)
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