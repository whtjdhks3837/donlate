package com.joe.donlate

import com.google.firebase.firestore.FirebaseFirestore
import com.joe.donlate.view.profile.ProfileSettingActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SettingViewTest {
    private val firebaseDatabase = FirebaseFirestore.getInstance()

    private lateinit var view: ProfileSettingActivity

    @Before
    fun setUp() {
        view = ProfileSettingActivity()
    }

    @Test
    fun registUser() {
        Single.just(
            firebaseDatabase.collection("user")
                .document("test")
                .parent
                .add(mapOf("uuid" to "testuuid", "name" to "testname"))
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        Mockito.mock(ProfileSettingActivity::class.java)
    }
}