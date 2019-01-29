package com.joe.donlate.view.meetings.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joe.donlate.data.Add
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingAddItemBinding
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.BaseHolder
import com.joe.donlate.view.MutableListAdapter

class MeetingsAdapter : MutableListAdapter<Meeting, BaseHolder<Meeting>>() {
    companion object {
        private const val ROOM_VIEW_TYPE = 0
        private const val ADD_VIEW_TYPE = 1
    }
    override val items: MutableList<Meeting> = mutableListOf()

    init {
        items.add(Add())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Meeting> =
        when (viewType) {
            ROOM_VIEW_TYPE -> MeetingsHolder(ListMeetingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> AddHolder(ListMeetingAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: BaseHolder<Meeting>, position: Int) {
        when (holder.itemViewType) {
            ROOM_VIEW_TYPE -> (holder as MeetingsHolder).bind(items[position])
            else -> (holder as AddHolder).bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position].viewType) {
            ROOM_VIEW_TYPE -> ROOM_VIEW_TYPE
            else -> ADD_VIEW_TYPE
        }
}