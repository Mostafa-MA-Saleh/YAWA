package com.gmail.mostafa.ma.saleh.yawa.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Mostafa on 11/11/2017.
 */

object PreferencesUtils {

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveString(key: String, string: String) = sharedPreferences?.run {
        edit().putString(key, string).apply()
    }

    @JvmOverloads
    fun getString(key: String, defValue: String = ""): String {
        return sharedPreferences?.getString(key, defValue) ?: defValue
    }
}
