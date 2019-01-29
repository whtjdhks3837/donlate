package com.joe.donlate.view.meetings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingsBinding
import com.joe.donlate.util.GlideUtil
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.util.toast
import com.joe.donlate.view.BaseActivity
import com.joe.donlate.view.meetings.list.MeetingsAdapter
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import com.joe.donlate.view_model.meetings.MeetingsViewModelFactory
import kotlinx.android.synthetic.main.activity_meetings.*
import org.koin.android.ext.android.inject

class MeetingsActivity : BaseActivity<ActivityMeetingsBinding>() {
    override val layoutResource: Int = R.layout.activity_meetings

    private val viewModel: MeetingsViewModel by lazy {
        val viewModelFactory: MeetingsViewModelFactory by inject("meetingsViewModelFactory")
        ViewModelProviders.of(this, viewModelFactory).get(MeetingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        getMeetings()
        initProfileImage()
    }

    private fun init() {
        list.apply {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            adapter = viewModel.listAdapter
        }
        roomsObserve()
        errorObserve()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun getMeetings() {
        viewModel.setProgress(true)
        viewModel.getMeetings(UuidUtil.getUuid(this))
    }

    private fun initProfileImage() {
        GlideUtil.loadFirebaseStorage(this, viewDataBinding.profileImage)
    }

    private fun roomsObserve() {
        viewModel.room.observe(this, Observer {
            it[0].participants[0].addSnapshotListener { a, b ->
                a?.let {
                    it.data?.let { data ->
                        Log.e("tag", "${data["name"]} ${data["uuid"]}")
                    }
                }
            }
            viewModel.setProgress(false)
            viewModel.listAdapter.preAdd(it)
        })
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer {
            viewModel.setProgress(false)
            toast(this, it)
        })
    }
}