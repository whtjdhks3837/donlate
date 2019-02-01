package com.joe.donlate.view.search_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.FragmentSearchPlaceBinding
import com.joe.donlate.util.toast
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view_model.meetings.MeetingsViewModel

class SearchPlaceFragment : BaseFragment<MeetingsActivity, FragmentSearchPlaceBinding>() {
    companion object {
        val instance = SearchPlaceFragment()
    }

    override var layoutResource: Int = R.layout.fragment_search_place
    private lateinit var activityViewModel: MeetingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        searchClickSubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.viewModel = activityViewModel
        return view
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
}