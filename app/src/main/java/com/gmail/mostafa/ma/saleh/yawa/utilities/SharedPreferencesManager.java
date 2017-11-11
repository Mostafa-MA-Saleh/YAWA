package com.gmail.mostafa.ma.saleh.yawa.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mostafa on 11/11/2017.
 */

public class SharedPreferencesManager {

    private static SharedPreferencesManager mInstance;

    private SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void init(Context context) {
        mInstance = new SharedPreferencesManager(context);
    }

    public static SharedPreferencesManager getInstance() {
        return mInstance;
    }

    public void saveString(String key, String string) {
        sharedPreferences.edit().putString(key, string).apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public String getTheme() {
        return SharedPreferencesManager.getInstance().getString("theme", "0");
    }
}
