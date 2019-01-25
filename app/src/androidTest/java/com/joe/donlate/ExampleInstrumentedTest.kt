package com.joe.donlate

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : ExampleInstrumentedTest{
    lateinit var context: Context
    val db = FirebaseFirestore.getInstance()
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        FirebaseApp.initializeApp(context)
    }

    @Test
    fun useAppContext() {
        Log.e("tag", "testestest")
        Single.create<Task<DocumentSnapshot>> { emitter ->
            db.collection("users")
                .document("test")
                .get()
                .addOnCompleteListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emitter.onError(Exception())
                }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("tag", "!!!!!")
            }, {
                it.printStackTrace()
                Log.e("tag", "error")
            })
    }
}