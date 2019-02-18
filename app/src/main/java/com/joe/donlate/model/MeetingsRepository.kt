package com.joe.donlate.model

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.joe.donlate.api.NaverMapService
import com.joe.donlate.data.Place
import com.joe.donlate.data.Meeting
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface MeetingsRepository {
    fun getMeetings(uuid: String): Single<QuerySnapshot>
    fun leaveMeeting(url: String, uuid: String): Single<QuerySnapshot>
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

    override fun leaveMeeting(url: String, uuid: String): Single<QuerySnapshot> =
        Single.create { emitter ->
            firebaseDatabase.collection("meetings")
                .whereEqualTo("url", url)
                .get()
                .addOnCompleteListener { task ->
                    task.result?.let { querySnapshot ->
                        val snapshot = querySnapshot.documents.first()
                        val newParticipants = (snapshot.data?.get("participants") as List<DocumentReference>)
                            .filter {
                                it.path != "users/$uuid"
                            }
                        if (newParticipants.isNotEmpty()) {
                            Log.e("tag", "뭐지..??")
                            snapshot.reference.update("participants", newParticipants)
                        } else {
                            Log.e("tag", "뭐지..")
                            //삭제
                            //snapshot.reference.delete()
                        }
                        emitter.onSuccess(querySnapshot)
                    } ?: emitter.onError(Exception())
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emitter.onError(Exception())
                }
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