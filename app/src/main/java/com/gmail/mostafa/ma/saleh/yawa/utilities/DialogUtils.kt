package com.gmail.mostafa.ma.saleh.yawa.utilities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar

/**
 * Created by Mostafa on 11/11/2017.
 */

object DialogUtils {
    fun showProgressDialog(context: Context): Dialog {
        Dialog(context).run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(ProgressBar(context))
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
            return this
        }
    }

    fun showListDialog(context: Context, items: List<String>, onClickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
                .setSingleChoiceItems(items.toTypedArray(), -1, onClickListener)
                .show()
    }
}
