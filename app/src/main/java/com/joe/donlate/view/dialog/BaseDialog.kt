package com.joe.donlate.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<T : ViewDataBinding>(context: Context) : Dialog(context) {
    private lateinit var binding: T
    protected abstract val resourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), resourceId, null, false)
        setContentView(binding.root)
    }
}