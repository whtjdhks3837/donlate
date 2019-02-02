package com.joe.donlate.view.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T: ViewDataBinding> : AppCompatActivity() {
    lateinit var viewDataBinding: T
    abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("tag", "activity create")
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResource)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.unbind()
        Log.e("tag", "activity destroy")
    }
}