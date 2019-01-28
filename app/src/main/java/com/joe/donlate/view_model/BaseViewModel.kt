package com.joe.donlate.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    protected val _error by lazy { MutableLiveData<String>() }
    protected val _progress by lazy { MutableLiveData<Boolean>() }
    val error: LiveData<String> by lazy { _error }
    val progress: LiveData<Boolean> by lazy { _progress }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun setProgress(isProgress: Boolean) {
        _progress.value = isProgress
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}