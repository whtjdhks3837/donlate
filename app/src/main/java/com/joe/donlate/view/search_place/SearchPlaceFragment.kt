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
import com.joe.donlate.view.search_place.list.AddressesAdapter
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_search_place.*

class SearchPlaceFragment : BaseFragment<MeetingsActivity, FragmentSearchPlaceBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = SearchPlaceFragment()
    }

    override var layoutResource: Int = R.layout.fragment_search_place
    private lateinit var activityViewModel: MeetingsViewModel
    private val addressesAdapter by lazy {
        AddressesAdapter { activityViewModel.setPlace(it) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        //TODO : EditText clear
        viewDataBinding.apply {
            list.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = addressesAdapter
            }
            viewModel = activityViewModel
        }
        searchClickSubscribe()
        searchPlaceResultSubscribe()
        activity.setOnFragmentKeyBackListener(this)
        return view
    }

    override fun onDestroyView() {
        addressesAdapter.clear()
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun searchClickSubscribe() {
        activityViewModel.searchPlaceClick.observe(this, Observer {
            Log.e("tag", "searchClickSubscribe")
            val text = viewDataBinding.placeEdit.text.toString()
            if (placeEditValidate(text))
                activityViewModel.searchPlace(text)
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
            addressesAdapter.set(it)
        })
    }

    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}