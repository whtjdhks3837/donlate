package com.joe.donlate.model

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Maybe
import io.reactivex.Single

interface ProfileSettingRepository {
    fun updateNickname(uuid: String, name: String): Maybe<Any>
    fun updateImage(uuid: String, image: Bitmap?): Single<UploadTask.TaskSnapshot>
    fun getMyAccount(uuid: String): Single<DocumentSnapshot>
}