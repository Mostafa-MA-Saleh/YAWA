package com.gmail.mostafa.ma.saleh.yawa.utilities

import android.app.Application

/**
 * Created by Mostafa on 11/11/2017.
 */

class YawaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}
