package com.joe.donlate.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("settingImage")
fun updateSettingImage(image: ImageView, bitmap: Bitmap?) {
    bitmap?.let { image.setImageBitmap(bitmap) }
}