package com.gmail.mostafa.ma.saleh.yawa.retrofit

import android.support.annotation.CallSuper

/**
 * Created by Mostafa on 10/23/2017.
 */

abstract class OnFinishedListener<T> {

    var isCancelled: Boolean = false
        private set

    open fun onSuccess(arg: T) {}

    open fun onFailure(message: String?) {}

    @CallSuper
    open fun onComplete(args: T?, message: String?) {
        if (!isCancelled) {
            if (args != null) {
                onSuccess(args)
            } else {
                onFailure(message)
            }
        }
    }

    fun cancel() {
        isCancelled = true
    }
}
