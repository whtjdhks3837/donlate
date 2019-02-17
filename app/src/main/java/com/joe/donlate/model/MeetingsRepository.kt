package com.joe.donlate.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.joe.donlate.api.NaverMapService
import com.joe.donlate.data.Place
import com.joe.donlate.data.Meeting
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface MeetingsRepository {
    fun getMeetings(uuid: String): Single<QuerySnapshot>
    fun leaveMeetings(vararg urls: String): Single<QuerySnapshot>
    fun createMeeting(uuid: String, meeting: Meeting): Single<DocumentReference>
    fun getAddress(query: String, count: Int = 20): Single<Place>
}

class MeetingsRepositoryImpl(private val naverMapService: NaverMapService) : MeetingsRepository {
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

    override fun leaveMeetings(vararg urls: String): Single<QuerySnapshot> =
        Single.create {
            firebaseDatabase.collection("meetings")
                .whereArrayContains("url", urls)
                .get()
        }


    override fun createMeeting(uuid: String, meeting: Meeting): Single<DocumentReference> =
        Single.create { emitter ->
            firebaseDatabase.collection("meetings")
                .add(meeting)
                .addOnSuccessListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    emitter.onError(Exception())
                }
        }

    override fun getAddress(query: String, count: Int): Single<Place> =
        naverMapService.getAddress(query, count)
}