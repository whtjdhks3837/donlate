package com.joe.donlate.view.meetingdetail

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingDetailBinding
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.viewmodel.meetingdetail.MeetingDetailViewModel
import com.joe.donlate.viewmodel.meetingdetail.MeetingDetailViewModelFactory
import com.naver.maps.map.MapFragment
import org.koin.android.ext.android.inject

class MeetingDetailActivity : BaseActivity<ActivityMeetingDetailBinding>() {
    override val layoutResource: Int = R.layout.activity_meeting_detail
    private lateinit var mapFragment: MapFragment
    private val viewModelFactory: MeetingDetailViewModelFactory by inject("meetingDetailViewModelFactory")
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MeetingDetailViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment? ?:
                MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().replace(R.id.map, it).commit()
                }

        val url = intent.getStringExtra("url")
        viewModel.loadMeetingDetail(url)
    }
}