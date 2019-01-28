package com.joe.donlate.model

import com.google.firebase.firestore.DocumentSnapshot
import com.joe.donlate.util.firebaseDatabase
import io.reactivex.Single

interface SplashRepository {
    fun getMyAccount(uuid: String): Single<DocumentSnapshot>
}

class SplashRepositoryImpl : SplashRepository {
    override fun getMyAccount(uuid: String): Single<DocumentSnapshot> =
        Single.create { emitter ->
            firebaseDatabase.collection("users")
                .document(uuid)
                .get()
                .addOnSuccessListener {
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    emitter.onError(Exception())
                }
        }
}