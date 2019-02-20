package com.joe.donlate.viewmodel.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.SplashRepository
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.SingleLiveData
import com.joe.donlate.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashViewModel(private val repository: SplashRepository) : BaseViewModel() {
    private val _user = SingleLiveData<Map<String, Any>>()
    private val _userNotFound = SingleLiveData<Any>()

    val user: LiveData<Map<String, Any>> = _user
    val userNotFound: LiveData<Any> = _userNotFound

    fun getMyAccount(uuid: String) {
        setProgress(true)
        compositeDisposable.add(
            repository.getMyAccount(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .doOnError { setProgress(false) }
                .subscribe({
                    val userNotFound = { _userNotFound.call() }
                    it.data?.let { data ->
                        _user.value = data
                    } ?: userNotFound.invoke()
                }, {
                    it.printStackTrace()
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }
}

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val repository: SplashRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }
}