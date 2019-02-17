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
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.text.SimpleDateFormat
import java.util.*

sealed class MeetingItemMode
object MeetingItemNormal : MeetingItemMode()
object MeetingItemDelete : MeetingItemMode()

class MeetingsAdapter(
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val meetingLongClick: (view: View) -> Unit
) : MutableListAdapter<Meeting, BaseHolder<Meeting>>() {
    companion object {
        var mode: MeetingItemMode = MeetingItemNormal
    }

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

    fun setMode(newMode: MeetingItemMode) {
        mode = newMode
        notifyDataSetChanged()
    }
}

class MeetingsHolder(
    private val binding: ListMeetingItemBinding,
    private val meetingClick: (meeting: Meeting) -> Unit,
    private val longClick: (view: View) -> Unit
) : BaseHolder<Meeting>(binding) {
    private val context = itemView.context
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ClickableViewAccessibility")
    override fun bind(data: Meeting) {
        val time = SimpleDateFormat("hh:mm").format(data.deadLine.toDate())
        val date = SimpleDateFormat("yyyy.MM.dd").format(data.deadLine.toDate())
        binding.apply {
            setMeetingTouch(meeting, data)
            setMeetingLongClick(meeting)
            setLeaveButton(leave, data)
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
            AnimationUtils.loadAnimation(context, R.anim.meeting_delete_mode_anim)
            longClick(itemView)
            false
        }
    }

    private fun setLeaveButton(view: CheckBox, data: Meeting) {
        when (MeetingsAdapter.mode) {
            is MeetingItemNormal -> {
                view.visibility = View.INVISIBLE
            }
            is MeetingItemDelete -> {
                view.visibility = View.VISIBLE
                setLeaveButtonCheckListener(view, data)
            }
        }
    }

    private fun setLeaveButtonCheckListener(view: CheckBox, data: Meeting) {
        //TODO : 잘 안눌리는거 고칠 것
        view.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.background = null
            data.isWaitLeave = !isChecked
            when (isChecked) {
                true -> {
                    buttonView.background = ContextCompat.getDrawable(context, R.drawable.ic_btn_delete_nor_black)
                }
                false -> {
                    buttonView.background = ContextCompat.getDrawable(context, R.drawable.ic_btn_delete_sel)
                }
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