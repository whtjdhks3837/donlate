package com.joe.donlate.model

import com.google.firebase.firestore.QuerySnapshot
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface MeetingsRepository {
    fun getMeetings(uuid: String): Single<QuerySnapshot>
}

class MeetingsRepositoryImpl : MeetingsRepository {
    override fun getMeetings(uuid: String): Single<QuerySnapshot> =
        Single.create { emitter ->
            firebaseDatabase.collection("meetings")
                .whereArrayContains("participants", firebaseDatabase.collection("users").document(uuid))
                .get()
                .addOnSuccessListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    emitter.onError(Exception())
                }
        }
}