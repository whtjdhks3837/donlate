package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.Timestamp
import com.joe.donlate.data.AddButton
import com.joe.donlate.data.MeetingItem
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingAddItemBinding
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.lang.Exception
import java.text.SimpleDateFormat
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

class MeetingsHolder(
    private val binding: ListMeetingItemBinding,
    private val meetingClick: (meeting: Meeting) -> Unit
) :
    BaseHolder<MeetingItem>(binding) {
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun bind(data: MeetingItem) {
        data as Meeting
        val time = SimpleDateFormat("hh:mm").format(data.deadLine.toDate())
        val date = SimpleDateFormat("yyyy.MM.dd").format(data.deadLine.toDate())
        itemView.setOnClickListener {
            meetingClick(data)
        }
        binding.title.text = data.title
        binding.time.text = timeConvert(time)
        binding.penaltyTime.text = "${data.penaltyTime}분"
        binding.penaltyFee.text = "${data.penaltyFee}₩"
        binding.numOfParticipants.text = "${data.participants.size}/${data.maxParticipants}"
        binding.date.text = date
    }

    private fun timeConvert(time: String): String {
        val timeSplit = time.split(":")
        val hour = timeSplit[0].toInt()
        val min = minConvert(timeSplit[1])
        return when (hour) {
            in 1..11 -> { "오전 $hour:$min" }
            12 -> { "오후 $hour:$min" }
            in 13..23 -> { "오후 ${hour - 12}:$min" }
            24 -> { "오전 0:$min" }
            else -> "invalid time"
        }
    }

    private fun minConvert(min: String) =
            when {
                min.length == 1 -> { "0$min" }
                else -> min
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