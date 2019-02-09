package com.joe.donlate.view_model.profile

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.donlate.model.ProfileSettingRepository
import com.joe.donlate.util.*
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileSettingViewModel(private val repository: ProfileSettingRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Map<String, Any>>()
    private val _updateNameClick = SingleLiveData<Any>()
    private val _updateName = MutableLiveData<Any>()
    private val _name = MutableLiveData<String>()
    private val _startMeetingsClick = SingleLiveData<Any>()
    private val _imageClick = SingleLiveData<Any>()
    private val _image = MutableLiveData<Bitmap>()
    private val _startMeetingsActivity = MutableLiveData<Any>()
    private val _clickable = MutableLiveData<Boolean>()

    val user: LiveData<Map<String, Any>> = _user
    val updateNameClick: LiveData<Any> = _updateNameClick
    val updateName: LiveData<Any> = _updateName
    val name: LiveData<String> = _name
    val startMeetingsClick: LiveData<Any> = _startMeetingsClick
    val imageClick: LiveData<Any> = _imageClick
    val image: LiveData<Bitmap> = _image
    val startMeetingsActivity: LiveData<Any> = _startMeetingsActivity
    val clickable: LiveData<Boolean> = _clickable

    fun updateName(uuid: String, name: String) {
        setProgress(true)
        compositeDisposable.add(
            repository.updateNickname(uuid, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},
                    {
                        it.printStackTrace()
                        error(NICKNAME_UPDATE_ERROR_MESSAGE)
                    }, {
                        setProgress(false)
                        _updateName.value = ""
                        _name.value = name
                    })
        )
    }

    fun updateImage(uuid: String, image: Bitmap) {
        setProgress(true)
        compositeDisposable.add(
            repository.updateImage(uuid, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setProgress(false)
                    _clickable.value = true
                    _image.value = image
                }, {
                    it.printStackTrace()
                    _clickable.value = true
                    error(IMAGE_UPLOAD_ERROR_MESSAGE)
                })
        )
    }

    private fun getAccount(uuid: String) =
        repository.getMyAccount(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyAccount(uuid: String) {
        setProgress(true)
        compositeDisposable.add(
            getAccount(uuid)
                .subscribe({
                    _clickable.value = true
                    setProgress(false)
                    it.data?.let { data ->
                        _user.value = data
                        _name.value = data["name"].toString()
                    }
                }, {
                    it.printStackTrace()
                    _clickable.value = true
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    fun checkAccount(uuid: String) {
        setProgress(true)
        compositeDisposable.add(
            getAccount(uuid)
                .subscribe({
                    _clickable.value = true
                    setProgress(false)
                    it.data?.let { _ ->
                        _startMeetingsActivity.value = ""
                    } ?: userNotFound()
                }, {
                    it.printStackTrace()
                    _clickable.value = true
                    error(SERVER_ERROR_MESSAGE)
                })
        )
    }

    private fun userNotFound() {
        error(USER_REGIST_FAILURE)
    }

    fun onStartMeetingsClick(view: View) {
        _clickable.value = false
        _startMeetingsClick.call()
    }

    fun onImageClick(view: View) {
        _clickable.value = false
        _imageClick.call()
    }

    fun onUpdateNicknameClick(view: View) {
        _clickable.value = false
        _updateNameClick.call()
    }

    fun setClickable(isClickable: Boolean) {
        _clickable.value = isClickable
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileSettingViewModelFactory(private val repository: ProfileSettingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ProfileSettingViewModel(repository) as T
}