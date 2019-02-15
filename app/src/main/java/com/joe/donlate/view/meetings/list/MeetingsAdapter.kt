package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.joe.donlate.R
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.text.SimpleDateFormat
import java.util.*

class MeetingsAdapter(
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val meetingLongClick: (view: View) -> Unit
) : MutableListAdapter<Meeting, BaseHolder<Meeting>>() {
    companion object {
        const val ROOM_VIEW_TYPE = 0x00
    }

    private var mode = ROOM_VIEW_TYPE
    override val items: LinkedList<Meeting> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Meeting> =
        MeetingsHolder(
            ListMeetingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), meetingClick, meetingLongClick
        )

    override fun onBindViewHolder(holder: BaseHolder<Meeting>, position: Int) {
        when (holder.itemViewType) {
            ROOM_VIEW_TYPE -> (holder as MeetingsHolder).bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    fun setMode(mode: Int) {
        this.mode = mode
    }
}

class MeetingsHolder(
    private val binding: ListMeetingItemBinding,
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val longClick: (view: View) -> Unit
) : BaseHolder<Meeting>(binding) {
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun bind(data: Meeting) {
        val time = SimpleDateFormat("hh:mm").format(data.deadLine.toDate())
        val date = SimpleDateFormat("yyyy.MM.dd").format(data.deadLine.toDate())
        itemView.setOnTouchListener { v, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    v.background =
                            ContextCompat.getDrawable(binding.root.context, R.drawable.meeting_item_touch_background)
                    Log.e("tag", "ACTION_DOWN")
                }
                event.action == MotionEvent.ACTION_UP -> {
                    v.background = ContextCompat.getDrawable(binding.root.context, R.drawable.meeting_item_background)
                    Log.e("tag", "ACTION_UP")
                    meetingClick(data)
                }
            }
            false
        }
        itemView.setOnLongClickListener {
            Log.e("tag", "LONGCLICK")
            longClick(itemView)
            false
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
            in 1..11 -> {
                "오전 $hour:$min"
            }
            12 -> {
                "오후 $hour:$min"
            }
            in 13..23 -> {
                "오후 ${hour - 12}:$min"
            }
            24 -> {
                "오전 0:$min"
            }
            else -> "invalid time"
        }
    }

    private fun minConvert(min: String) =
        when {
            min.length == 1 -> {
                "0$min"
            }
            else -> min
        }
}