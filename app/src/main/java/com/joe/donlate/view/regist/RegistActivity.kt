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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityRegistBinding
import com.joe.donlate.util.REQUEST_IMAGE_CODE
import com.joe.donlate.util.Utils
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

        viewModel.registClick.observe(this, Observer {
            val name = viewDataBinding.nameEdit.text.toString()
            if (name == "") {
                Toast.makeText(this, "이름이 공백입니다.", Toast.LENGTH_SHORT).show()
                return@Observer
            }
            if (name.length !in 2..6) {
                Toast.makeText(this, "2자에서 6자 사이로 입력해주세요..", Toast.LENGTH_SHORT).show()
                return@Observer
            }
            viewModel.regist("01099623521", name)
        })

         viewModel.imageClick.observe(this, Observer {
             val intent = Intent()
             intent.type = "image/*"
             intent.action = Intent.ACTION_GET_CONTENT
             startActivityForResult(intent, REQUEST_IMAGE_CODE)
         })


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
            == PackageManager.PERMISSION_DENIED) {
            Log.e("tag", "권한없어")
        } else {
            Log.e("tag", (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number)
        }
        
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
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

    private fun checkPhoneNumberPermission() =
            if (!Utils.getPhoneNumberPermission(this)) {

            } else {

            }
    /* Todo : 권한 추가 */
    /*private fun getMyPhoneNumber(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val phoneNumber = telephonyManager.line1Number
        return phoneNumber
    }*/
}