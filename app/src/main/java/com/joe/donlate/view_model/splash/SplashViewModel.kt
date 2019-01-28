package com.joe.donlate.view_model.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.data.User
import com.joe.donlate.model.SplashRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashViewModel(private val repository: SplashRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Map<String, Any>>()
    private val _userNotFound = MutableLiveData<Any>()

    val user: LiveData<Map<String, Any>> = _user
    val userNotFound: LiveData<Any> = _userNotFound

    fun getMyAccount(uuid: String) {
        addDisposable(
            repository.getMyAccount(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.data?.let { data ->
                        _user.value = data
                    } ?: userNotFound()
                }, {
                    it.printStackTrace()
                    _error.value = SERVER_ERROR_MESSAGE
                })
        )
    }

    private fun userNotFound() {
        _userNotFound.value = ""
    }
}

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val repository: SplashRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }
}