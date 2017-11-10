package com.gmail.mostafa.ma.saleh.yawa.retrofit;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * Created by Mostafa on 10/23/2017.
 */

public abstract class OnFinishedListener<T> {

    private boolean cancelled;

    public void onSuccess(T arg) {
    }

    public void onFailure(String message) {
    }

    @CallSuper
    public void onComplete(@Nullable T args, @Nullable String message) {
        if (!cancelled) {
            if (args != null) {
                onSuccess(args);
            } else {
                onFailure(message);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
