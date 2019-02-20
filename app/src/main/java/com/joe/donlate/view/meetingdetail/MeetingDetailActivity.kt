package com.joe.donlate.view.meetingdetail

import android.os.Bundle
import com.joe.donlate.R
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ActivityMeetingDetailBinding
import com.joe.donlate.view.base.BaseActivity
import com.naver.maps.map.MapFragment

class MeetingDetailActivity : BaseActivity<ActivityMeetingDetailBinding>() {
    override val layoutResource: Int = R.layout.activity_meeting_detail
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment? ?:
                MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().replace(R.id.map, it).commit()
                }
        val meeting = intent.getParcelableExtra<Meeting>("meeting")
        meeting
    }
}