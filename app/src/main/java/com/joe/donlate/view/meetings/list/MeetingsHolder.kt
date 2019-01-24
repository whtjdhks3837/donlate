package com.joe.donlate.view.meetings.list

import com.joe.donlate.data.Room
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.BaseHolder

class MeetingsHolder(private val binding: ListMeetingItemBinding) : BaseHolder<Room>(binding)  {
    override fun bind(data: Room) {
        binding.title.text = data.title
    }
}