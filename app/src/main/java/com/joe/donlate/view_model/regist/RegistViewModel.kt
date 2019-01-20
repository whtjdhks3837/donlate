package com.joe.donlate.view_model.regist

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.joe.donlate.model.RegistRepository
import com.joe.donlate.util.IMAGE_UPLOAD_ERROR_MESSAGE
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.USER_UPDATE_ERROR_MESSAGE
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class RegistViewModel(private val repository: RegistRepository) : BaseViewModel() {
    private val _regist = MutableLiveData<Array<Boolean>>()
    private val _registClick = MutableLiveData<Any>()
    private val _imageClick = MutableLiveData<Any>()
    private val _image = MutableLiveData<Bitmap>()
    private val _error = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val regist: LiveData<Array<Boolean>> = _regist
    val registClick: LiveData<Any> = _registClick
    val imageClick: LiveData<Any> = _imageClick
    val image: LiveData<Bitmap> = _image
    val error: LiveData<String> = _error
    val progress: LiveData<Boolean> = _progress

    init {
        _regist.value = Array(2) { false }
    }

    private fun registUser(uuid: String, name: String) =
        repository.registUser(uuid, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    private fun registImage(uuid: String, image: Bitmap?) =
        repository.registImage(uuid, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun regist(uuid: String, name: String) {
        addDisposable(Single.zip(
            registUser(uuid, name),
            registImage(uuid, image.value),
            BiFunction<Task<DocumentReference>, UploadTask, Pair<Task<DocumentReference>, UploadTask>> { task, upload ->
                Pair(task, upload)
            }
        ).subscribe({ pair ->
            Log.e("tag", "subscribe")
            pair.first.addOnFailureListener {
                Log.e("tag", "upload fail")
                it.printStackTrace()
                _error.value = USER_UPDATE_ERROR_MESSAGE
            }.addOnCompleteListener {
                _regist.value!![0] = it.isSuccessful
                Log.e("tag", "complete1 ${it.isSuccessful}")
            }

            pair.second.addOnFailureListener {
                Log.e("tag", "upload fail")
                it.printStackTrace()
                _error.value = IMAGE_UPLOAD_ERROR_MESSAGE
            }.addOnCompleteListener {
                Log.e("tag", "complete2 ${it.isComplete}")
                _regist.value!![1] = it.isComplete
            }
        }, {
            it.printStackTrace()
            _error.value = SERVER_ERROR_MESSAGE
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

    fun setProgress(isLoading: Boolean) {
        _progress.value = isLoading
    }

    fun setInitRegistState() {
        _regist.value!![0] = false
        _regist.value!![1] = false
    }
}