package com.joe.donlate.view.meetings.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joe.donlate.data.Room
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.MutableListAdapter

class MeetingsAdapter : MutableListAdapter<Room, MeetingsHolder>() {
    override val items: MutableList<Room> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MeetingsHolder(ListMeetingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}