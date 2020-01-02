package com.zxc.tutorials.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionHelper {

    private Activity mActivity;
    private PermissionCallback mPermissionCallback;
    private ArrayList<String> permission_list = new ArrayList<>();
    private ArrayList<String> listPermissionsNeeded = new ArrayList<>();
    private String dialogContent = "";
    private int requestCode;

    public PermissionHelper(Context mContext, PermissionCallback callback) {
        this.mActivity = (Activity) mContext;
        this.mPermissionCallback = callback;
    }

    public void processPermission(ArrayList<String> permissions, String dialogContent, int requestCode) {
        this.permission_list = permissions;
        this.dialogContent = dialogContent;
        this.requestCode = requestCode;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permissions, requestCode)) {
                mPermissionCallback.onPermissionResult(requestCode, PermissionEnum.GRANTED);
            }
        } else {
            mPermissionCallback.onPermissionResult(requestCode, PermissionEnum.GRANTED);
        }
    }

    private boolean checkAndRequestPermissions(ArrayList<String> permissions, int request_code) {
        if (permissions.size() > 0) {
            listPermissionsNeeded = new ArrayList<>();
            for (int i = 0; i < permissions.size(); i++) {
                int hasPermission = ContextCompat.checkSelfPermission(mActivity, permissions.get(i));
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), request_code);
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == this.requestCode) {
            if (grantResults.length > 0) {
                Map<String, Integer> perms = new HashMap<>();
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                final ArrayList<String> pendingPermissions = new ArrayList<>();
                for (int i = 0; i < listPermissionsNeeded.size(); i++) {
                    if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, listPermissionsNeeded.get(i)))
                            pendingPermissions.add(listPermissionsNeeded.get(i));
                        else {
                            mPermissionCallback.onPermissionResult(this.requestCode, PermissionEnum.NEVER_ASK_AGAIN);
                            settingsDialog();
                            return;
                        }
                    }
                }
                if (pendingPermissions.size() > 0) {
                    showMessageOKCancel(dialogContent,
                            (dialog, which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        processPermission(permission_list, dialogContent, PermissionHelper.this.requestCode);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        if (permission_list.size() == pendingPermissions.size())
                                            mPermissionCallback.onPermissionResult(PermissionHelper.this.requestCode, PermissionEnum.DENIED);
                                        else
                                            mPermissionCallback.onPermissionResult(PermissionHelper.this.requestCode, PermissionEnum.PARTIALLY_GRANTED);
                                        break;
                                }
                            });
                } else {
                    mPermissionCallback.onPermissionResult(this.requestCode, PermissionEnum.GRANTED);
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void settingsDialog() {

    }
}
