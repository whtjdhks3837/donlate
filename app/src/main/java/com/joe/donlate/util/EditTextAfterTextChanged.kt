package com.joe.donlate.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData

fun EditText.afterTextChangedUpdateLiveData(liveData: MutableLiveData<String>) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(edit: Editable?) {
            val text = edit?.toString() ?: ""
            liveData.value = text
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}