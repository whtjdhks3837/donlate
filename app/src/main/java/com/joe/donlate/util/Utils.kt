package com.joe.donlate.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.InputStream

const val FIRESTORAGE_URL = "gs://donlate-66efb.appspot.com/"
val firebaseAuth = FirebaseAuth.getInstance()
val firebaseDatabase = FirebaseFirestore.getInstance()
val firebaseStorage = FirebaseStorage.getInstance(FIRESTORAGE_URL)

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

object PhoneUtil {
    @SuppressLint("HardwareIds")
    fun getPhoneNumber(context: Context, grant: (number: String) -> Unit, request: () -> Unit) {
        val manager = getTelephonyManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                grant(manager.line1Number.replace("+82", "0"))
            } else {
                request()
            }
        } else {
            grant(manager.line1Number.replace("+82", "0"))
        }
    }

    private fun getTelephonyManager(context: Context) =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}

object UuidUtil {
    @SuppressLint("HardwareIds")
    fun getUuid(context: Context) =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)!!
}

object BitmapUtil {
    fun resize(context: Context, uri: Uri, inSampleSize: Int): Bitmap? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeStream(inputStream, null, options)
    }
}

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