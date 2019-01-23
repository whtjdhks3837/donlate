package com.joe.donlate.view_model.profile

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joe.donlate.model.ProfileSettingRepository
import com.joe.donlate.util.IMAGE_UPLOAD_ERROR_MESSAGE
import com.joe.donlate.util.SERVER_ERROR_MESSAGE
import com.joe.donlate.util.USER_REGIST_FAILURE
import com.joe.donlate.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileSettingViewModel(private val repository: ProfileSettingRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Map<String, Any>>()
    private val _startMeetingsClick = MutableLiveData<Any>()
    private val _imageClick = MutableLiveData<Any>()
    private val _image = MutableLiveData<Bitmap>()
    private val _startMeetingsActivity = MutableLiveData<Any>()
    private val _error = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val user: LiveData<Map<String, Any>> = _user
    val startMeetingsClick: LiveData<Any> = _startMeetingsClick
    val imageClick: LiveData<Any> = _imageClick
    val image: LiveData<Bitmap> = _image
    val startMeetingsActivity: LiveData<Any> = _startMeetingsActivity
    val error: LiveData<String> = _error
    val progress: LiveData<Boolean> = _progress

    private fun updateUser(uuid: String, name: String) =
        repository.updateUser(uuid, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateImage(uuid: String, image: Bitmap) {
        addDisposable(
            repository.updateImage(uuid, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setProgress(false)
                    _image.value = image
                }, {
                    it.printStackTrace()
                    _error.value = IMAGE_UPLOAD_ERROR_MESSAGE
                })
        )
    }

    private fun getAccount(uuid: String) =
        repository.getMyAccount(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyAccount(uuid: String) {
        addDisposable(
            getAccount(uuid)
                .subscribe({
                    it.data?.let { data ->
                        _user.value = data
                    } ?: setProgress(false)
                }, {
                    it.printStackTrace()
                    _error.value = SERVER_ERROR_MESSAGE
                })
        )
    }

    fun checkAccount(uuid: String) {
        addDisposable(
            getAccount(uuid)
                .subscribe({
                    it.data?.let { _ ->
                        _startMeetingsActivity.value = ""
                    } ?: userNotFound()
                }, {
                    it.printStackTrace()
                    _error.value = SERVER_ERROR_MESSAGE
                })
        )
    }

    private fun userNotFound() {
        _error.value = USER_REGIST_FAILURE
    }

    fun onStartMeetingsClick(view: View) {
        _startMeetingsClick.value = ""
    }

    fun onImageClick(view: View) {
        _imageClick.value = ""
    }

    fun setProgress(isLoading: Boolean) {
        _progress.value = isLoading
    }
}