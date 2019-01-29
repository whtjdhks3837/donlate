package com.joe.donlate

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.joe.donlate.data.Room
import com.joe.donlate.model.MeetingsRepositoryImpl
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MeetingsInstrumentedMeeting {
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
                val room = it.toObjects(Room::class.java)
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