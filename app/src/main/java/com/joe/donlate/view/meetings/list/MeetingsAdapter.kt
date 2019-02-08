package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.joe.donlate.data.AddButton
import com.joe.donlate.data.MeetingItem
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingAddItemBinding
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.util.*

class MeetingsAdapter(private val meetingClick: (meeting: Meeting) -> Unit, private val addClick: () -> Unit) :
    MutableListAdapter<MeetingItem, BaseHolder<MeetingItem>>() {
    companion object {
        private const val ROOM_VIEW_TYPE = 0
        private const val ADD_VIEW_TYPE = 1
    }

    override val items: LinkedList<MeetingItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<MeetingItem> =
        when (viewType) {
            ROOM_VIEW_TYPE -> MeetingsHolder(
                ListMeetingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                meetingClick
            )
            else -> AddHolder(
                ListMeetingAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                addClick
            )
        }

    override fun onBindViewHolder(holder: BaseHolder<MeetingItem>, position: Int) {
        when (holder.itemViewType) {
            ROOM_VIEW_TYPE -> (holder as MeetingsHolder).bind(items[position])
            else -> (holder as AddHolder).bind(AddButton())
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int =
        when (position < items.size) {
            true -> ROOM_VIEW_TYPE
            false -> ADD_VIEW_TYPE
        }
}

class MeetingsHolder(private val binding: ListMeetingItemBinding, private val meetingClick: (meeting: Meeting) -> Unit) :
    BaseHolder<MeetingItem>(binding) {
    @SuppressLint("SetTextI18n")
    override fun bind(data: MeetingItem) {
        data as Meeting
        itemView.setOnClickListener {
            meetingClick(data)
        }
        binding.title.text = data.title
        binding.penaltyTime.text = "${data.penaltyTime}분당"
        binding.penaltyFee.text = "${data.penaltyFee}원"
    }
}

class AddHolder(private val binding: ListMeetingAddItemBinding, private val addClick: () -> Unit) :
    BaseHolder<MeetingItem>(binding) {
    override fun bind(data: MeetingItem) {
        itemView.setOnClickListener {
            addClick()
        }
    }
}