package com.joe.donlate.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions

object GlideUtil {
    fun loadFirebaseStorage(context: Context, imageView: ImageView) {
        val storageReference =
            firebaseStorage.getReferenceFromUrl("${FIRESTORAGE_URL}/images/${UuidUtil.getUuid(context)}.jpg")
        GlideApp.with(context)
            .load(storageReference)
            .apply(RequestOptions().circleCrop())
            .into(imageView)
    }
}