package com.joe.donlate

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.joe.donlate.data.Meeting
import com.joe.donlate.model.MeetingsRepositoryImpl
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Maybe
import io.reactivex.Single
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

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        FirebaseApp.initializeApp(context)
    }

    @Test
    fun existJoinRoom() {
        val countDownLatch = CountDownLatch(1)
        /*disposable.add(repository.getMeetings("f12851f785162a16")
            .subscribe({
                val room = it.toObjects(Meeting::class.java)
                assert(!room.isEmpty())
                countDownLatch.countDown()
            }, {
                it.printStackTrace()
                countDownLatch.countDown()
            })
        )*/
        countDownLatch.await()
    }

    @Test
    fun nonExistJoinRoom() {
        /*val countDownLatch = CountDownLatch(1)
        disposable.add(repository.getMeetings("nonUser")
            .subscribe({

            }, {

            }))*/
    }

    @Test
    fun leaveMeetings() {
        val countDownLatch = CountDownLatch(1)
        println("leaveMeetings")
        val urls = listOf("test2")
        firebaseDatabase
            .collection("meetings")
            .whereArrayContains("participants", firebaseDatabase.collection("users").document("f19ef37fac0525a0"))
            .get()
            .addOnCompleteListener { task ->
                Log.e("tag", "success ${task.result?.size()}")
                task.result?.let { querySnapshot ->
                    querySnapshot.documents
                        .filter { urls.contains(it["url"]) }
                        .forEach {
                            Log.e("tag", "${it.get("title")}")
                            it.reference.update("participants",
                                (it.data?.get("participants") as List<DocumentReference>).filter {
                                    it.path != "users/f12851f785162a16"
                                })
                        }
                    countDownLatch.countDown()
                } ?: countDownLatch.countDown()
            }
            .addOnFailureListener {
                println("fail")
                countDownLatch.countDown()
            }
        countDownLatch.await()
    }

    @After
    fun tearDown() {
        disposable.clear()
    }
}