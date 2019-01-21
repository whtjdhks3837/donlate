package com.joe.donlate.view_model.profile

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joe.donlate.model.RegistRepository
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class RegistViewModel(private val repository: RegistRepository) : BaseViewModel() {
    private val _regist = MutableLiveData<Stack<Pair<String, Boolean>>>()
    private val _registClick = MutableLiveData<Any>()
    private val _imageClick = MutableLiveData<Any>()
    private val _image = MutableLiveData<Bitmap>()
    private val _error = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val regist: LiveData<Stack<Pair<String, Boolean>>> = _regist
    val registClick: LiveData<Any> = _registClick
    val imageClick: LiveData<Any> = _imageClick
    val image: LiveData<Bitmap> = _image
    val error: LiveData<String> = _error
    val progress: LiveData<Boolean> = _progress

    private fun registUser(uuid: String, name: String) =
        repository.registUser(uuid, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun registImage(uuid: String, image: Bitmap?) =
        repository.registImage(uuid, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

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
        _regist.value!!.clear()
    }
}