package com.gmail.mostafa.ma.saleh.yawa.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by Mostafa on 11/11/2017.
 */

public class DialogUtils {
    public static Dialog showProgressDialog(@NonNull Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(new ProgressBar(context));
        //noinspection ConstantConditions
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void showListDialog(@NonNull Context context, @NonNull List<String> items,
                                      DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setSingleChoiceItems(items.toArray(new String[0]), -1, onClickListener)
                .show();
    }
}
