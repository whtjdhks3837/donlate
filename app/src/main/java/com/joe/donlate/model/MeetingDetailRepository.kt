package com.joe.donlate.model

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface MeetingDetailRepository {
    fun loadMeetingDetail(url: String): Single<QuerySnapshot>
}

class MeetingDetailRepositoryImpl : MeetingDetailRepository {
    override fun loadMeetingDetail(url: String): Single<QuerySnapshot> =
        Single.create { emitter ->
            firebaseDatabase.collection("meetings")
                .whereEqualTo("url", url)
                .get()
                .addOnSuccessListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emitter.onError(Exception())
                }
        }
}