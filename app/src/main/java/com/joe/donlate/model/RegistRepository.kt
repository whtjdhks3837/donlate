package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import io.reactivex.Single

interface RegistRepository {
    fun registUser(phone: String, name: String): Single<Task<DocumentReference>>
    fun registImage(phone: String, image: Bitmap?): Single<StorageReference>
}