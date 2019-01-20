package com.joe.donlate.view.regist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityRegistBinding
import com.joe.donlate.util.REQUEST_IMAGE_CODE
import com.joe.donlate.util.REQUEST_PHONE_STATE_CODE
import com.joe.donlate.util.Utils
import com.joe.donlate.util.toast
import com.joe.donlate.view.BaseActivity
import com.joe.donlate.view_model.regist.RegistViewModel
import com.joe.donlate.view_model.regist.RegistViewModelFactory
import org.koin.android.ext.android.get

class RegistActivity : BaseActivity<ActivityRegistBinding>() {
    override val layoutResource: Int = R.layout.activity_regist
    private val viewModel: RegistViewModel by lazy {
        ViewModelProviders.of(this, RegistViewModelFactory(get())).get(RegistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registObserve()
        errorObserve()
        registClickObserve()
        imageClickObserve()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun registClickObserve() {
        viewModel.registClick.observe(this, Observer {
            regist(viewDataBinding.nameEdit.text.toString())
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

    private fun registObserve() {
        viewModel.regist.observe(this, Observer { complete ->
            if ((complete[0] && complete[1]) || (complete[0] && !complete[1])) {
                Log.e("tag", "sucesssssss")
                viewModel.setProgress(false)
                viewModel.setInitRegistState()
            } else if (!complete[0] && complete[1]) {
                Log.e("tag", "faillllllll")
                viewModel.setProgress(false)
                viewModel.setInitRegistState()
            }
        })
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer {
            viewModel.setProgress(false)
            toast(this, it)
        })
    }

    private fun regist(name: String) {
        if (registValidate(name)) {
            viewModel.setProgress(true)
            viewModel.regist(Utils.Uuid.getUuid(this), viewDataBinding.nameEdit.text.toString())
        }
    }

    private fun registValidate(name: String): Boolean {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { _ ->
                val bitmap = getResizeBitmap(data.data!!)
                bitmap?.let {
                    viewModel.setImageView(it)
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
            viewModel.regist(phone, viewDataBinding.nameEdit.text.toString())
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
                    viewModel.regist(phone, viewDataBinding.nameEdit.text.toString())
                }, {})
            } else {
                toast(this, "권한체크 해주삼ㅠ")
            }
        }
    }*/
}