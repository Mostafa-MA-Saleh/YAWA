package com.gmail.mostafa.ma.saleh.yawa.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.BuildConfig;
import com.gmail.mostafa.ma.saleh.yawa.R;


public class AboutDialog extends Dialog {

    private AboutDialog(@NonNull Context context) {
        super(context);
    }

    public static void show(Context context) {
        AboutDialog aboutDialog = new AboutDialog(context);
        aboutDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_about);
        setTitle(R.string.about);
        TextView txtAppVersion = findViewById(R.id.txt_app_version);
        txtAppVersion.setText(BuildConfig.VERSION_NAME);
    }
}
