package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.joe.donlate.util.firebasesStore
import io.reactivex.Single

class RegistRepositoryImpl : RegistRepository {
    override fun registUser(phone: String, name: String): Single<Task<DocumentReference>> =
        Single.just(
            firebasesStore.collection("user")
                .add(mapOf("phone" to phone, "name" to name))
        )

    override fun registImage(image: Bitmap): Single<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}