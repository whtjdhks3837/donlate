package com.joe.donlate.view_model.regist

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.joe.donlate.model.RegistRepository
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class RegistViewModel(private val repository: RegistRepository) : BaseViewModel() {
    private val _regist = MutableLiveData<Any>()
    private val _registClick = MutableLiveData<Any>()
    private val _imageClick = MutableLiveData<Any>()
    private val _image = MutableLiveData<Bitmap>()
    private val _error = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val regist: LiveData<Any> = _regist
    val registClick: LiveData<Any> = _registClick
    val imageClick: LiveData<Any> = _imageClick
    val image: LiveData<Bitmap> = _image
    val error: LiveData<String> = _error
    val progress: LiveData<Boolean> = _progress

    private fun registUser(phone: String, name: String) =
        repository.registUser(phone, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    private fun registImage(phone: String, image: Bitmap?) =
        repository.registImage(phone, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun regist(phone: String, name: String) {
        addDisposable(Single.zip(
            registUser(phone, name)
                .doOnError {
                    it.printStackTrace()
                    Log.e("tag", "registUserError")
                },
            registImage(phone, image.value)
                .doOnError {
                    it.printStackTrace()
                    Log.e("tag", "registImageError")
                },
            BiFunction<Task<DocumentReference>, StorageReference, Pair<Task<DocumentReference>, StorageReference>> { task, storage ->
                Pair(task, storage)
            }
        ).subscribe({
            Log.e("tag", "subscribe")
            it.first.addOnSuccessListener {
                Log.e("tag", "addOnSuccessListener")
            }
            it.first.addOnFailureListener {
                it.printStackTrace()
                Log.e("tag", "addOnFailureListener")
            }
        }, {
            it.printStackTrace()
            Log.e("Tag", "throw")
        })
        )
    }

    fun setImageView(bitmap: Bitmap) {
        _image.value = bitmap
    }

    fun onRegistClick(view: View) {
        _registClick.value = ""
    }

    fun onImageClick(view: View) {
        _imageClick.value = ""
    }
}