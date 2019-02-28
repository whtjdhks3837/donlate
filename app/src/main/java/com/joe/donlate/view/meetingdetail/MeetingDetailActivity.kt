package com.joe.donlate.view.meetingdetail

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.donlate.R
import com.joe.donlate.databinding.ActivityMeetingDetailBinding
import com.joe.donlate.util.DateUtil
import com.joe.donlate.util.TimeUtil
import com.joe.donlate.view.base.BaseActivity
import com.joe.donlate.viewmodel.meetingdetail.MeetingDetailViewModel
import com.joe.donlate.viewmodel.meetingdetail.MeetingDetailViewModelFactory
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import org.koin.android.ext.android.inject

class MeetingDetailActivity : BaseActivity<ActivityMeetingDetailBinding>() {
    override val layoutResource: Int = R.layout.activity_meeting_detail
    private lateinit var mapFragment: MapFragment
    private val viewModelFactory: MeetingDetailViewModelFactory by inject("meetingDetailViewModelFactory")
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MeetingDetailViewModel::class.java)
    }

    private var mapCamera: CameraUpdate? = null
    private var mapMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment? ?:
                MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().replace(R.id.map, it).commit()
                }

        mapFragment.getMapAsync { naverMap ->
            mapCamera?.let {
                naverMap.moveCamera(it)
            }
            /*mapMarker?.let { Marker(). }
            Marker().apply {
                position = latLng
                map = it
            }*/
        }
        val url = intent.getStringExtra("url")
        viewModel.loadMeetingDetail(url)
        meetingSubscribe()

        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun meetingSubscribe() {
        viewModel.meeting.observe(this, Observer { data ->
            val date = DateUtil.from(data.deadLine.toDate())
            val time = TimeUtil.from(data.deadLine.toDate())
            viewDataBinding.date.text = date
            viewDataBinding.time.text = time
            val lat = data.coordinate.latitude
            val lon = data.coordinate.longitude
            val latLng = LatLng(lat, lon)
            mapCamera = CameraUpdate.scrollTo(latLng)
            mapMarker = Marker().apply {
                position = latLng
            }
        })
    }
}