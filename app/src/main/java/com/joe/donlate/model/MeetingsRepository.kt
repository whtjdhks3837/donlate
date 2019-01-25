package com.joe.donlate.model

import com.google.firebase.firestore.DocumentSnapshot
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface MeetingsRepository {
    fun getMeetings(uuid: String): Single<DocumentSnapshot>
}

class MeetingsRepositoryImpl : MeetingsRepository {
    override fun getMeetings(uuid: String): Single<DocumentSnapshot> =
        Single.create { emitter ->
            firebaseDatabase.collection("meetings")
                .document()
                .get()
                .addOnSuccessListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    emitter.onError(Exception())
                }
        }
}