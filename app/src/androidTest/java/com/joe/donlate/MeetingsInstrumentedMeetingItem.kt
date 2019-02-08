package com.joe.donlate

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.joe.donlate.data.Meeting
import com.joe.donlate.model.MeetingsRepositoryImpl
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MeetingsInstrumentedMeetingItem {
    private lateinit var context: Context
    private val disposable = CompositeDisposable()
    private val repository = MeetingsRepositoryImpl()

    val observable = Maybe.just("status")
        .filter {
            it != ""
        }
        .map {

        }

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        FirebaseApp.initializeApp(context)
    }

    @Test
    fun existJoinRoom() {
        val countDownLatch = CountDownLatch(1)
        disposable.add(repository.getMeetings("f12851f785162a16")
            .subscribe({
                val room = it.toObjects(Meeting::class.java)
                assert(!room.isEmpty())
                countDownLatch.countDown()
            }, {
                it.printStackTrace()
                countDownLatch.countDown()
            })
        )
        countDownLatch.await()
    }

    @Test
    fun nonExistJoinRoom() {
        val countDownLatch = CountDownLatch(1)
        disposable.add(repository.getMeetings("nonUser")
            .subscribe({

            }, {

            }))
    }

    @Test
    fun foo() {

        observable.subscribe({

        }, {

        })
    }

    @After
    fun tearDown() {
        disposable.clear()
    }
}