package com.joe.donlate.model

import com.joe.donlate.util.firebaseAuth
import io.reactivex.Single

class SplashRepositoryImpl : SplashRepository {
    private val defaultPassword = "donlate"

    override fun getMyAccount(uuid: String) =
        Single.just(firebaseAuth.signInWithEmailAndPassword(uuid, defaultPassword))!!
}