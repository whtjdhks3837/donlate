package com.joe.donlate.view.dialog

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.core.app.ActivityCompat
import com.joe.donlate.R
import com.joe.donlate.databinding.DialogPermissionBinding
import com.joe.donlate.util.REQUEST_PHONE_STATE_CODE

class PermissionDialog(context: Context) : BaseDialog<DialogPermissionBinding>(context) {
    override val resourceId = R.layout.dialog_permission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}