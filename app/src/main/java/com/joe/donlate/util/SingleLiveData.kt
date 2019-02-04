package com.joe.donlate.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.joe.donlate.view_model.CLICK
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasObservers()) {
//            throw Throwable("Only one observer at a time may subscribe to a SingleLiveData")
            Log.e("tag", "hasObservers")
        }
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false))
                observer.onChanged(it)
        })

    }

    @MainThread
    fun call() {
        value = null
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}