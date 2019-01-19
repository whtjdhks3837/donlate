package com.joe.donlate.view_model.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joe.donlate.model.SplashRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashViewModel(private val repository: SplashRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Pair<String, String>>()
    private val _userNotFound = MutableLiveData<Any>()
    private val _error = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val user: LiveData<Pair<String, String>> = _user
    val userNotFound: LiveData<Any> = _userNotFound
    val error: LiveData<String> = _error
    val progress: LiveData<Boolean> = _progress

    fun getMyAccount(uuid: String) {
        addDisposable(repository.getMyAccount(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            _user.value = Pair("1234", "1234")
                        }
                        !task.isSuccessful -> {
                            _userNotFound.value = ""
                        }
                    }
                }
            }, {
                it.printStackTrace()
                _error.value = SERVER_ERROR_MESSAGE
            })
        )
    }

    fun setProgress(isLoading: Boolean) {
        _progress.value = isLoading
    }
}