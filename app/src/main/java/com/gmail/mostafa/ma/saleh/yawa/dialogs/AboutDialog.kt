package com.gmail.mostafa.ma.saleh.yawa.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView

import com.gmail.mostafa.ma.saleh.yawa.BuildConfig
import com.gmail.mostafa.ma.saleh.yawa.R


class AboutDialog private constructor(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_about)
        setTitle(R.string.about)
        findViewById<TextView>(R.id.app_version_text_view).text = BuildConfig.VERSION_NAME
    }

    companion object {
        fun show(context: Context) = AboutDialog(context).show()
    }
}
