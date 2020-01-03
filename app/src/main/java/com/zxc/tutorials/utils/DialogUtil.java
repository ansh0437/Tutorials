package com.zxc.tutorials.utils;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.zxc.tutorials.interfaces.DialogListener;

public class DialogUtil {

    public static void alert(Activity activity, String message) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle("Alert");
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        mBuilder.setNegativeButton("Cancel", null);
        mBuilder.setCancelable(true);
        mBuilder.show();
    }

    public static void confirmationAlert(
            Activity activity,
            String title,
            String message,
            String positiveLabel,
            String negativeLabel,
            boolean isCancellable,
            DialogListener dialogListener
    ) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(positiveLabel, (dialog, which) -> dialogListener.onPositiveClick(dialog));
        mBuilder.setNegativeButton(negativeLabel, (dialog, which) -> dialogListener.onNegativeClick(dialog));
        mBuilder.setCancelable(isCancellable);
        mBuilder.show();
    }

}
