package com.joe.donlate.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import io.reactivex.Single

interface SplashRepository {
    fun getMyAccount(uuid: String) : Single<Task<AuthResult>>
}