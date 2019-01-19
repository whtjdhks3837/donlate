package com.joe.donlate.view_model.regist

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    private fun registImage(image: Bitmap) =
        repository.registImage(image)
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

    fun regist(phone: String, name: String) {
        addDisposable(repository.registUser(phone, name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ task ->
                task.addOnSuccessListener {
                    Log.e("tag", "${it.parent.id} ${it.id}")
                }
                task.addOnFailureListener {
                    it.printStackTrace()
                }
            }, {
                it.printStackTrace()
            })
        )
    }
}