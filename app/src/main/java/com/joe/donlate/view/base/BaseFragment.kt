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
import com.joe.donlate.databinding.FragmentMeetingsBinding
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_create_meeting.*

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
        Log.e("tag", "$tag state onCreate: ${lifecycle.currentState.name}")
        Log.e("tag", "stack : ${activity.supportFragmentManager.backStackEntryCount}")
    }

    /**
     * onCreate와 onCreateView 사이에 lifecycle의 이벤트 CREATED 호출
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("tag", "$tag onCreateView")
        Log.e("tag", "$tag state onCreateView: ${lifecycle.currentState.name}")
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResource, container, false) as R
        return viewDataBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("tag", "$tag onDestroyView")
        Log.e("tag", "$tag state onDestroyView: ${lifecycle.currentState.name}")
        /*viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        viewDataBinding.setLifecycleOwner(null)*/
    }

    /**
     * onDestroyView onDetach 사이에 lifecycle의 이벤트 DESTROY 호출
     */

    override fun onDetach() {
        super.onDetach()
        Log.e("tag", "$tag onDetach")
        Log.e("tag", "$tag state onDetach: ${lifecycle.currentState.name}")
    }
}