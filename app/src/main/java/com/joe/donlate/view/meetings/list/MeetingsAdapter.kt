package com.joe.donlate.view.meetings.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.joe.donlate.data.AddButton
import com.joe.donlate.data.Meeting
import com.joe.donlate.data.Room
import com.joe.donlate.databinding.ListMeetingAddItemBinding
import com.joe.donlate.databinding.ListMeetingItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import com.joe.donlate.view_model.CLICK

class MeetingsAdapter(private val startCreateMeeting: MutableLiveData<Any>) :
    MutableListAdapter<Meeting, BaseHolder<Meeting>>() {
    companion object {
        private const val ROOM_VIEW_TYPE = 0
        private const val ADD_VIEW_TYPE = 1
    }

    override val items: MutableList<Meeting> = mutableListOf()

    init {
        items.add(AddButton())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Meeting> =
        when (viewType) {
            ROOM_VIEW_TYPE -> MeetingsHolder(
                ListMeetingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> AddHolder(
                ListMeetingAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                startCreateMeeting
            )
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

class AddHolder(private val binding: ListMeetingAddItemBinding, private val startCreateMeeting: MutableLiveData<Any>) :
    BaseHolder<Meeting>(binding) {
    override fun bind(data: Meeting) {
        data as AddButton
        itemView.setOnClickListener {
            startCreateMeeting.value = CLICK
        }
    }
}

class MeetingsHolder(private val binding: ListMeetingItemBinding) : BaseHolder<Meeting>(binding) {
    @SuppressLint("SetTextI18n")
    override fun bind(data: Meeting) {
        data as Room
        binding.title.text = data.title
        binding.penaltyTime.text = "${data.penaltyTime}분당"
        binding.penaltyFee.text = "${data.penaltyFee}원"
    }
}