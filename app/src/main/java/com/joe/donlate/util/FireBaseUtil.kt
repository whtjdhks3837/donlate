package com.joe.donlate.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

const val FIRESTORAGE_URL = "gs://donlate-66efb.appspot.com/"

val firebaseAuth = FirebaseAuth.getInstance()
val firebaseDatabase = FirebaseFirestore.getInstance()
val firebaseStorage = FirebaseStorage.getInstance(FIRESTORAGE_URL)