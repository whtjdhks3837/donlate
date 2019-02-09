package com.joe.donlate

import com.google.firebase.Timestamp
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class MeetingsTest {
    @Test
    fun timeStampConvertTest() {
        println("${Timestamp.now().toDate()}")
        println(SimpleDateFormat("hh:mm").format(Timestamp.now().toDate()))
    }
}