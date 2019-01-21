package com.joe.donlate.model

import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.Single

interface SplashRepository {
    fun getMyAccount(uuid: String) : Single<DocumentSnapshot>
}