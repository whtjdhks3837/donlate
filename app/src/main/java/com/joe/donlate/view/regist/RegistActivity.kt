package com.joe.donlate.view.regist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityRegistBinding
import com.joe.donlate.util.REQUEST_IMAGE_CODE
import com.joe.donlate.util.REQUEST_PHONE_STATE_CODE
import com.joe.donlate.util.Utils
import com.joe.donlate.view.BaseActivity
import com.joe.donlate.view.dialog.PermissionDialog
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

    private fun regist(name: String) {
        if (registValidate(name))
            checkPhoneStatePermission()
    }

    private fun registValidate(name: String): Boolean {
        if (name == "") {
            Toast.makeText(this, "이름이 공백입니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (name.length !in 2..6) {
            Toast.makeText(this, "2자에서 6자 사이로 입력해주세요..", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkPhoneStatePermission() {
        Utils.getPhoneStatePermission(this, { phone ->
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
                Utils.getPhoneStatePermission(this, { phone ->
                    viewModel.regist(phone, viewDataBinding.nameEdit.text.toString())
                }, {})
            } else {
                Toast.makeText(this, "권한체크 해주삼ㅠ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                val inputStream = contentResolver.openInputStream(data.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.setImageView(bitmap)
            }
        }
    }
}