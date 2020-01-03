package com.zxc.tutorials.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxc.tutorials.interfaces.DialogListener;
import com.zxc.tutorials.utils.DialogUtil;

public class PermissionHelper {

    private final int REQUEST_CODE_SETTING = 1;

    private Activity mActivity;
    private Fragment mFragment;
    private PermissionCallback mPermissionCallback;

    private boolean isActivity = true;

    private int iRequestCode;
    private String mPermission;

    public PermissionHelper(Activity mActivity, PermissionCallback permissionCallback) {
        this.mActivity = mActivity;
        this.mPermissionCallback = permissionCallback;
    }

    public PermissionHelper(Fragment mFragment, PermissionCallback permissionCallback) {
        this.mFragment = mFragment;
        this.mActivity = mFragment.requireActivity();
        this.isActivity = false;
        this.mPermissionCallback = permissionCallback;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void askPermission(int requestCode, String permission) {
        this.iRequestCode = requestCode;
        this.mPermission = permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isPermissionGranted(mPermission)) {
            requestPermission();
        } else {
            mPermissionCallback.onGranted(iRequestCode);
        }
    }

    private void requestPermission() {
        if (isActivity) {
            ActivityCompat.requestPermissions(mActivity, new String[]{mPermission}, iRequestCode);
        } else {
            mFragment.requestPermissions(new String[]{mPermission}, iRequestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == iRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionCallback.onGranted(iRequestCode);
            } else {
                mPermissionCallback.onDenied(iRequestCode, isActivity
                        ? !ActivityCompat.shouldShowRequestPermissionRationale(mActivity, mPermission)
                        : !mFragment.shouldShowRequestPermissionRationale(mPermission));
            }
        }
    }

    public void askAgainDialog(String message) {
        DialogUtil.confirmationAlert(mActivity, "Alert", message, "Ok",
                "Cancel", false, new DialogListener() {
                    @Override
                    public void onNegativeClick(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onPositiveClick(DialogInterface dialogInterface) {
                        requestPermission();
                        dialogInterface.dismiss();
                    }
                }
        );
    }

    public void neverAskDialog() {
        DialogUtil.confirmationAlert(mActivity, "Alert",
                "Give permission manually from settings.", "Ok",
                "Cancel", false, new DialogListener() {
                    @Override
                    public void onNegativeClick(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onPositiveClick(DialogInterface dialogInterface) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                        mActivity.startActivityForResult(intent, REQUEST_CODE_SETTING);
                        dialogInterface.dismiss();
                    }
                }
        );
    }
}
