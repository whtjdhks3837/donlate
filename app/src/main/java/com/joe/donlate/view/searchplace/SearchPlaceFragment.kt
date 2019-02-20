package com.joe.donlate.view.searchplace

import android.os.Bundle
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
import com.joe.donlate.util.showToast
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meetings_main.MeetingsActivity
import com.joe.donlate.view.searchplace.list.AddressesAdapter
import com.joe.donlate.viewmodel.meetings.MeetingsViewModel
import com.joe.donlate.viewmodel.meetings.SearchPlaceInput
import com.joe.donlate.viewmodel.meetings.SearchPlaceOutput

class SearchPlaceFragment : BaseFragment<MeetingsActivity, FragmentSearchPlaceBinding>(), OnFragmentKeyBackListener {
    companion object {
        val instance = SearchPlaceFragment()
    }

    private val addressesAdapter by lazy { AddressesAdapter { activityViewModel.setPlace(it) } }
    override var layoutResource: Int = R.layout.fragment_search_place
    private lateinit var activityViewModel: MeetingsViewModel
    private lateinit var activityViewModelOutput: SearchPlaceOutput
    private lateinit var activityViewModelInput: SearchPlaceInput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        activityViewModelOutput = activityViewModel.searchPlaceOutput
        activityViewModelInput = activityViewModel.searchPlaceInput
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.apply {
            list.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = addressesAdapter
            }
            viewModel = activityViewModel
            setLifecycleOwner(viewLifecycleOwner)
        }
        activity.setOnFragmentKeyBackListener(this)
        searchPlaceObserve()
        searchPlaceResultObserve()
        startCreateMeetingObserve()
        return view
    }

    override fun onDestroyView() {
        addressesAdapter.clear()
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun searchPlaceObserve() {
        activityViewModelOutput.searchPlace.observe(this, Observer {
            val text = viewDataBinding.placeEdit.text.toString()
            if (placeEditValidate(text))
                activityViewModel.searchPlace(text)
        })
    }

    private fun placeEditValidate(text: String) =
        when (text) {
            "" -> {
                activity.showToast("공백없이 입력해주세요.")
                false
            }
            else -> true
        }

    private fun searchPlaceResultObserve() {
        activityViewModelOutput.searchPlaceResult.observe(this, Observer {
            addressesAdapter.set(it)
        })
    }

    private fun startCreateMeetingObserve() {
        activityViewModelOutput.startCreateMeetingFragment.observe(this, Observer {
            startCreateMeeting()
        })
    }

    private fun startCreateMeeting() =
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun onBack(stackName: String?) {
        startCreateMeeting()
    }
}