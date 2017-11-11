package com.gmail.mostafa.ma.saleh.yawa.utilities;

import android.app.Application;

/**
 * Created by Mostafa on 11/11/2017.
 */

public class YawaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
    }
}
