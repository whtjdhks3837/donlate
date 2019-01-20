package com.joe.donlate.model

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import com.joe.donlate.util.firebaseDatabase
import com.joe.donlate.util.firebaseStorage
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RegistRepositoryImpl : RegistRepository {
    override fun registUser(uuid: String, name: String): Single<Task<DocumentReference>> =
        Single.just(
            firebaseDatabase.collection("user")
                .document(uuid)
                .parent
                .add(mapOf("uuid" to uuid, "name" to name))
        )

    override fun registImage(uuid: String, image: Bitmap?): Single<UploadTask> {
        return image?.let {
            val bos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()
            val bs = ByteArrayInputStream(bitmapData)
            return Single.just(
                firebaseStorage.reference.child("images/$uuid.jpg")
                    .putStream(bs)
            )
        } ?: Single.just(firebaseStorage.reference.putFile(Uri.EMPTY))
    }
}