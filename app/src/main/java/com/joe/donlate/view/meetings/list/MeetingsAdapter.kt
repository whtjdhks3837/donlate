package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.joe.donlate.R
import com.joe.donlate.data.Meeting
import com.joe.donlate.data.MeetingItemDeleteMode
import com.joe.donlate.data.MeetingItemNormalMode
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.text.SimpleDateFormat
import java.util.*


class MeetingsAdapter(
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val meetingLongClick: (view: View, position: Int) -> Unit
) : MutableListAdapter<Meeting, BaseHolder<Meeting>>() {
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
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setAnimation(position: Int) {
        initMode()
        setDeleteMode(position)
    }

    fun initMode() {
        items.find {
            it.mode is MeetingItemDeleteMode
        }?.let {
            it.mode = MeetingItemNormalMode
            notifyItemChanged(items.indexOf(it))
        }
    }

    private fun setDeleteMode(position: Int) {
        items[position].mode = MeetingItemDeleteMode
        notifyItemChanged(position)
    }
}

class MeetingsHolder(
    private val binding: ListMeetingItemBinding,
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val longClick: (view: View, position: Int) -> Unit
) : BaseHolder<Meeting>(binding) {
    private val context = itemView.context
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ClickableViewAccessibility")
    override fun bind(data: Meeting) {
        val time = SimpleDateFormat("hh:mm").format(data.deadLine.toDate())
        val date = SimpleDateFormat("yyyy.MM.dd").format(data.deadLine.toDate())

        binding.apply {
            setMeetingTouch(meeting, data)
            setMeetingLongClick(meeting)
            setMode(leave, data)
            title.text = data.title
            this.time.text = timeConvert(time)
            penaltyTime.text = "${data.penaltyTime}분"
            penaltyFee.text = "${data.penaltyFee}₩"
            numOfParticipants.text = "${data.participants.size}/${data.maxParticipants}"
            this.date.text = date
        }
    }

    private fun setMeetingTouch(view: View, data: Meeting) {
        view.setOnTouchListener { v, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    v.background =
                            ContextCompat.getDrawable(context, R.drawable.meeting_item_touch_background)
                }
                event.action == MotionEvent.ACTION_UP -> {
                    v.background =
                            ContextCompat.getDrawable(context, R.drawable.meeting_item_background)
                    meetingClick(data)
                }
            }
            false
        }
    }

    private fun setMeetingLongClick(view: View) {
        view.setOnLongClickListener {
            longClick(itemView, adapterPosition)
            false
        }
    }

    private fun setMode(view: CheckBox, data: Meeting) {
        when (data.mode) {
            is MeetingItemNormalMode -> {
                itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.meeting_normal_mode_anim))
                view.visibility = View.INVISIBLE
            }
            is MeetingItemDeleteMode -> {
                itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.meeting_delete_mode_anim))
                view.visibility = View.VISIBLE
            }
        }
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