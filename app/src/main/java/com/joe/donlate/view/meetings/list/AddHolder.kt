package com.joe.donlate.view.meetings.list

import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingAddItemBinding
import com.joe.donlate.view.BaseHolder

class AddHolder(private val binding: ListMeetingAddItemBinding) : BaseHolder<Meeting>(binding) {
    override fun bind(data: Meeting) {}
}