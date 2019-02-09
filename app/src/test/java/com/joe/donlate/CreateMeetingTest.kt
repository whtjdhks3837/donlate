package com.joe.donlate

import kotlinx.android.synthetic.main.fragment_create_meeting.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class CreateMeetingTest {
    @Before
    fun setUp() {

    }

    @Test
    fun dateValidateTest() {
        val lesserDate = getInputTime("2019", "02", "09", "10", "00")
        val biggerDate = getInputTime("2019", "03", "03", "10", "00")
        val current = getCurrentTime()
        println("$lesserDate $current")
        println("$biggerDate $current")
        println("${Date(lesserDate)} ${Date(biggerDate)}")
        assert(lesserDate < current)
        assert(biggerDate > current)
    }

    private fun getInputTime(year: String, month: String, day: String, hour: String, min: String): Long {
        return SimpleDateFormat("yyyyMMddHHmm").parse("$year$month$day$hour$min").time
    }

    private fun getCurrentTime(): Long {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = appendZero((calendar.get(Calendar.MONTH) + 1).toString())
        val day = appendZero(calendar.get(Calendar.DAY_OF_MONTH).toString())
        val hour = appendZero(calendar.get(Calendar.HOUR_OF_DAY).toString())
        val min = appendZero(calendar.get(Calendar.MINUTE).toString())
        println("$year $month $day $hour $min")
        return SimpleDateFormat("yyyyMMddHHmm").parse("$year$month$day$hour$min").time
    }

    private fun appendZero(month: String) =
        if (month.length < 2) {
            val convertMonth = "0$month"
            convertMonth
        } else {
            month
        }
}