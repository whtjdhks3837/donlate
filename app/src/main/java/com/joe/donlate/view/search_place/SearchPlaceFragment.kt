package com.joe.donlate.view.search_place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joe.donlate.R
import com.joe.donlate.databinding.FragmentSearchPlaceBinding
import com.joe.donlate.util.toast
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.create_meeting.CreateMeetingFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view_model.meetings.MeetingsViewModel

class SearchPlaceFragment : BaseFragment<MeetingsActivity, FragmentSearchPlaceBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = SearchPlaceFragment()
    }

    override var layoutResource: Int = R.layout.fragment_search_place
    private lateinit var activityViewModel: MeetingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.list.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = activityViewModel.addressesAdapter
        }
        viewDataBinding.viewModel = activityViewModel
        searchClickSubscribe()
        searchPlaceResultSubscribe()
        return view
    }

    override fun onStart() {
        super.onStart()
        activity.setOnFragmentKeyBackListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun searchClickSubscribe() {
        activityViewModel.searchPlaceClick.observe(this, Observer {
            val text = viewDataBinding.placeEdit.text.toString()
            if (placeEditValidate(text)) {
                activityViewModel.searchPlace(text)
            }
        })
    }

    private fun placeEditValidate(text: String) =
        when (text) {
            "" -> {
                toast(activity, "공백없이 입력해주세요.")
                false
            }
            else -> true
        }

    private fun searchPlaceResultSubscribe() {
        activityViewModel.searchPlaceResult.observe(this, Observer {
            activityViewModel.addressesAdapter.set(it)
        })
    }

    //TODO("back stack error 해결")
    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}