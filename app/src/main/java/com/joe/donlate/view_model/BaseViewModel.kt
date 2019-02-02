package com.joe.donlate.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

const val CLICK = ""
open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _error by lazy { MutableLiveData<String>() }
    private val _progress by lazy { MutableLiveData<Boolean>() }
    val error: LiveData<String> by lazy { _error }
    val progress: LiveData<Boolean> by lazy { _progress }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun setProgress(isProgress: Boolean) {
        _progress.value = isProgress
    }

    fun error(message: String) {
        setProgress(false)
        _error.value = message
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("tag", "onCleared")
        compositeDisposable.clear()
    }
}