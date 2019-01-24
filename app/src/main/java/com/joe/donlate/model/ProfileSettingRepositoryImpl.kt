package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.util.firebaseStorage
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ProfileSettingRepositoryImpl : ProfileSettingRepository {
    override fun updateNickname(uuid: String, name: String): Maybe<Any> =
        Maybe.create { emitter ->
            firebaseDatabase.collection("users")
                .document(uuid)
                .set(mapOf("name" to name))
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emitter.onError(Exception())
                }
        }

    override fun updateImage(uuid: String, image: Bitmap?): Single<UploadTask.TaskSnapshot> =
        image?.let { bitmap ->
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()
            val bs = ByteArrayInputStream(bitmapData)
            return Single.create { emitter ->
                firebaseStorage.reference.child("images/$uuid.jpg")
                    .putStream(bs)
                    .addOnSuccessListener {
                        emitter.onSuccess(it)
                    }
                    .addOnFailureListener {
                        emitter.onError(Exception())
                    }
            }
        } ?: Single.create { emitter ->
            emitter.onError(Exception())
        }

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