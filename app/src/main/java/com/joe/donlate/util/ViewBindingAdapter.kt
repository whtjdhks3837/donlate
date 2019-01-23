package com.joe.donlate.util

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("settingImage")
fun updateSettingImage(image: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        image.setImageBitmap(bitmap)
        Glide.with(image.context)
            .load(bitmap)
            .apply(RequestOptions().circleCrop())
            .into(image)
    }
}