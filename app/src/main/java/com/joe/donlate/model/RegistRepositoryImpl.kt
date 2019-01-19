package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.util.firebaseStorage
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception

class RegistRepositoryImpl : RegistRepository {
    override fun registUser(phone: String, name: String): Single<Task<DocumentReference>> =
        Single.just(
            firebaseDatabase.collection("user")
                .document(phone)
                .parent
                .add(mapOf("phone" to phone, "name" to name))
        )
//        Single.error(Exception())

    override fun registImage(phone: String, image: Bitmap?): Single<StorageReference> {
        return image?.let {
            return Single.just(
                firebaseStorage.reference.child("images/$phone.jpg")
            )
        } ?: Single.just(firebaseStorage.reference)
    }
}