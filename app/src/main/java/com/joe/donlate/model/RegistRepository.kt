package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Maybe
import io.reactivex.Single

interface RegistRepository {
    fun registUser(uuid: String, name: String): Single<Task<DocumentReference>>
    fun registImage(uuid: String, image: Bitmap?): Single<UploadTask>
}