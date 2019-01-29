package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import com.joe.donlate.data.Room
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.BaseHolder

class MeetingsHolder(private val binding: ListMeetingItemBinding) : BaseHolder<Meeting>(binding)  {
    @SuppressLint("SetTextI18n")
    override fun bind(data: Meeting) {
        data as Room
        binding.title.text = data.title
        binding.penaltyTime.text = "${data.penaltyTime}분당"
        binding.penaltyFee.text = "${data.penaltyFee}원"
    }
}