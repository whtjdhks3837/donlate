package com.joe.donlate.view.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T: BaseActivity<*>, R: ViewDataBinding> : Fragment() {
    protected lateinit var activity: T
    protected lateinit var viewDataBinding: R
    abstract var layoutResource: Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("tag", "$tag onAttach")
        activity = context as T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("tag", "$tag onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("tag", "$tag onCreateView")
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
        return viewDataBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("tag", "$tag onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("tag", "$tag onDetach")
    }
}