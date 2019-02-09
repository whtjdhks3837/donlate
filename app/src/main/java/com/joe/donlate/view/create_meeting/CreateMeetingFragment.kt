package com.joe.donlate.view.create_meeting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.ResourcePath
import com.joe.donlate.R
import com.joe.donlate.data.Meeting
import com.joe.donlate.databinding.FragmentCreateMeetingBinding
import com.joe.donlate.util.FORMAT_INVALID
import com.joe.donlate.util.UuidUtil
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.util.toast
import com.joe.donlate.view.OnFragmentKeyBackListener
import com.joe.donlate.view.base.BaseFragment
import com.joe.donlate.view.meeting_main.MeetingsActivity
import com.joe.donlate.view.search_place.SearchPlaceFragment
import com.joe.donlate.view_model.meetings.MeetingsViewModel
import kotlinx.android.synthetic.main.fragment_create_meeting.*
import java.text.SimpleDateFormat
import java.util.*

class CreateMeetingFragment : BaseFragment<MeetingsActivity, FragmentCreateMeetingBinding>(),
    OnFragmentKeyBackListener {
    companion object {
        val instance = CreateMeetingFragment()
        private const val MAX_OF_PARTICIPANTS = 20
        private const val MAX_OF_PENALTY_TIME = 10
        private const val MAX_OF_PENALTY_FEE = 5000
        private const val TITLE_EMPTY = "제목을 입력해주세요."
        private const val DATE_LESSER = "현재시간 이후의 시간을 입력해주세요."
        private const val YEAR_EMPTY = "약속 년도를 입력해주세요."
        private const val YEAR_FORMAT_INVALID = "약속 년도의 $FORMAT_INVALID"
        private const val MONTH_EMPTY = "약속 월을 입력해주세요."
        private const val MONTH_FORMAT_INVALID = "약속 월의 $FORMAT_INVALID"
        private const val DAY_EMPTY = "약속 일을 입력해주세요."
        private const val DAY_FORMAT_INVALID = "약속 일의 $FORMAT_INVALID"
        private const val HOUR_EMPTY = "시간을 입력해주세요."
        private const val HOUR_FORMAT_INVALID = "약속 시간의 $FORMAT_INVALID"
        private const val MIN_EMPTY = "분을 입력해주세요"
        private const val MIN_FORMAT_INVALID = "약속 분의 $FORMAT_INVALID"
        private const val PARTICIPANTS_NUM_EMPTY = "참여인원을 입력해주세요."
        private const val PARTICIPANTS_MAX_OVER = "최대 참여인원은 ${MAX_OF_PARTICIPANTS}명입니다."
        private const val PENALTY_TIME_OVER = "최대 지각비 시간은 ${MAX_OF_PENALTY_TIME}분입니다."
        private const val PENALTY_TIME_EMPTY = "지각비 시간을 입력해주세요."
        private const val PENALTY_FEE_OVER = "최대 분당 지각비는 ${MAX_OF_PENALTY_FEE}원입니다"
        private const val PENALTY_FEE_EMPTY = "최대 분당 지각비를 입력해주세요."
        private const val PLACE_NOT_FOUND = "약속장소를 입력해주세요."
    }

    override var layoutResource: Int = R.layout.fragment_create_meeting
    private lateinit var activityViewModel: MeetingsViewModel
    private val calendar by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(activity).get(MeetingsViewModel::class.java)
        activityViewModel.initCreateMeetingBindingData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.apply {
            viewModel = activityViewModel
            setLifecycleOwner(viewLifecycleOwner)
        }
        activity.setOnFragmentKeyBackListener(this)
        createMeetingSubscribe()
        createMeetingClickSubscribe()
        placeSubscribe()
        startSearchPlaceClickSubscribe()
        Log.e("tag", "${SimpleDateFormat("yyyy-MM-dd").parse("2019-01-11").time}")
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.setOnFragmentKeyBackListener(null)
    }

    private fun createMeetingSubscribe() {
        activityViewModel.createMeeting.observe(this, Observer {
            activityViewModel.addRoom(it)
            activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })
    }

    private fun placeSubscribe() {
        activityViewModel.place.observe(this, Observer {
            /*it?.let { address ->
                viewDataBinding.searchPlace.setText(address.jibun)
            } ?: viewDataBinding.searchPlace.setText()*/
        })
    }

    private fun createMeetingClickSubscribe() {
        activityViewModel.createMeetingClick.observe(this, Observer {
            makeMeetingRoom()?.let { meetingRoom ->
                activityViewModel.createMeeting(UuidUtil.getUuid(activity), meetingRoom)
            }
        })
    }

    private fun makeMeetingRoom(): Meeting? =
        if (CreateValidator().validate()) {
            Meeting(
                title = editTitle.text.toString(),
                createAt = Timestamp.now(),
                deadLine = Timestamp(
                    Date(SimpleDateFormat("yyyy-MM-dd").parse("${year.text}-${month.text}-${day.text}").time)),
                coordinate = GeoPoint(
                    activityViewModel.place.value!!.lat.toDouble(),
                    activityViewModel.place.value!!.lon.toDouble()),
                maxParticipants = maxParticipants.text.toString().toInt(),
                url = makeUrl(),
                penaltyTime = penaltyTime.text.toString().toInt(),
                penaltyFee = penaltyFee.text.toString().toInt(),
                participants = listOf(
                    DocumentReference.forPath(
                        ResourcePath.fromString("/users/${UuidUtil.getUuid(activity)}"),
                        firebaseDatabase
                    )
                )
            )
        } else {
            null
        }

    private fun makeUrl() = "testurl"

    private fun startSearchPlaceClickSubscribe() {
        activityViewModel.startSearchPlaceClick.observe(viewLifecycleOwner, Observer {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SearchPlaceFragment.instance, MeetingsActivity.FragmentTag.SEARCH_PLACE)
                .addToBackStack(MeetingsActivity.FragmentTag.SEARCH_PLACE)
                .commit()
        })
    }

    override fun onBack(stackName: String?) {
        activity.supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    inner class CreateValidator {
        fun validate() = when {
            isTitleEmpty() -> {
                toast(activity, TITLE_EMPTY)
                false
            }
            isYearEmpty() -> {
                toast(activity, YEAR_EMPTY)
                false
            }
            isYearInvalid() -> {
                toast(activity, YEAR_FORMAT_INVALID)
                false
            }
            isMonthEmpty() -> {
                toast(activity, MONTH_EMPTY)
                false
            }
            isMonthInvalid() -> {
                toast(activity, MONTH_FORMAT_INVALID)
                false
            }
            isDayEmpty() -> {
                toast(activity, DAY_EMPTY)
                false
            }
            isDayInvalid() -> {
                toast(activity, DAY_FORMAT_INVALID)
                false
            }
            isHourEmpty() -> {
                toast(activity, HOUR_EMPTY)
                false
            }
            isHourInvalid() -> {
                toast(activity, HOUR_FORMAT_INVALID)
                false
            }
            isMinEmpty() -> {
                toast(activity, MIN_EMPTY)
                false
            }
            isMinInvalid() -> {
                toast(activity, MIN_FORMAT_INVALID)
                false
            }
            isNumOfParticipantsEmpty() -> {
                toast(activity, PARTICIPANTS_NUM_EMPTY)
                false
            }
            numOfParticipantsValidate() -> {
                toast(activity, PARTICIPANTS_MAX_OVER)
                false
            }
            isPenaltyTimeEmpty() -> {
                toast(activity, PENALTY_TIME_EMPTY)
                false
            }
            penaltyTimeValidate() -> {
                toast(activity, PENALTY_TIME_OVER)
                false
            }
            isPenaltyFeeEmpty() -> {
                toast(activity, PENALTY_FEE_EMPTY)
                false
            }
            penaltyFeeValidate() -> {
                toast(activity, PENALTY_FEE_OVER)
                false
            }
            isDateLesser() -> {
                toast(activity, DATE_LESSER)
                false
            }
            isPlaceEmpty() -> {
                toast(activity, PLACE_NOT_FOUND)
                false
            }
            else -> true
        }

        private fun isTitleEmpty() = editTitle.text?.isEmpty() ?: false
        private fun isYearEmpty() = year.text.isEmpty()
        private fun isMonthEmpty() = month.text.isEmpty()
        private fun isDayEmpty() = day.text.isEmpty()
        private fun isHourEmpty() = hour.text.isEmpty()
        private fun isMinEmpty() = min.text.isEmpty()
        private fun isNumOfParticipantsEmpty() = maxParticipants.text.isEmpty()
        private fun isPenaltyTimeEmpty() = penaltyTime.text.isEmpty()
        private fun isPenaltyFeeEmpty() = penaltyFee.text.isEmpty()
        private fun isYearInvalid() = year.text.toString().length != 4
        private fun isMonthInvalid() = month.text.toString().length != 2
                || month.text.toString().toInt() !in 1..12

        private fun isDayInvalid() = day.text.toString().length != 2
                || day.text.toString().toInt() !in 1..getMaxDayOfMonth()

        private fun isHourInvalid() = hour.text.toString().length != 2
                || hour.text.toString().toInt() !in 0..23

        private fun isMinInvalid() = min.text.toString().length != 2
                || min.text.toString().toInt() !in 0..59

        private fun isDateLesser(): Boolean = getInputTime() <= getCurrentTime()
        private fun numOfParticipantsValidate() = maxParticipants.text.toString().toInt() > MAX_OF_PARTICIPANTS
        private fun penaltyTimeValidate() = penaltyTime.text.toString().toInt() > MAX_OF_PENALTY_TIME
        private fun penaltyFeeValidate() = penaltyTime.text.toString().toInt() > MAX_OF_PENALTY_FEE
        private fun isPlaceEmpty() = searchPlace.text.isEmpty()

        private fun getMaxDayOfMonth(): Int {
            val year = year.text.toString().toInt()
            val month = month.text.toString().toInt() - 1
            val calendar = Calendar.getInstance()
            calendar.set(year, month, Calendar.DAY_OF_MONTH)
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        private fun getInputTime(): Long {
            val year = year.text.toString()
            val month = month.text.toString()
            val day = day.text.toString()
            val hour = hour.text.toString()
            val min = min.text.toString()
            return SimpleDateFormat("yyyyMMddHHmm").parse("$year$month$day$hour$min").time
        }

        private fun getCurrentTime(): Long {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR).toString()
            val month = appendZero((calendar.get(Calendar.MONTH) + 1).toString())
            val day = appendZero(calendar.get(Calendar.DAY_OF_MONTH).toString())
            val hour = appendZero(calendar.get(Calendar.HOUR_OF_DAY).toString())
            val min = appendZero(calendar.get(Calendar.MINUTE).toString())
            return SimpleDateFormat("yyyyMMddHHmm").parse("$year$month$day$hour$min").time
        }

        private fun appendZero(month: String) = when {
            (month.length < 2) -> "0$month"
            else -> month
        }


    }
}