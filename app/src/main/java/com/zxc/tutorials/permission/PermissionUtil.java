package com.zxc.tutorials.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtil {

    private Activity mActivity;
    private Fragment mFragment;
    private PermissionCallback mPermissionCallback;

    private boolean isActivity = true;

    private int iRequestCode = 19;
    private String mPermission;

    public PermissionUtil(Activity mActivity, PermissionCallback mPermissionCallback) {
        this.mActivity = mActivity;
        this.mPermissionCallback = mPermissionCallback;
    }

    public PermissionUtil(Fragment mFragment, PermissionCallback mPermissionCallback) {
        this.mFragment = mFragment;
        this.mActivity = mFragment.requireActivity();
        this.isActivity = false;
        this.mPermissionCallback = mPermissionCallback;
    }

    public void askPermission(String permission) {
        mPermission = permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isPermissionGranted()) {
            requestPermission();
        } else {
            mPermissionCallback.onPermissionResult(iRequestCode, PermissionEnum.GRANTED);
        }
    }

    private void requestPermission() {
        if (isActivity) {
            ActivityCompat.requestPermissions(mActivity, new String[]{mPermission}, iRequestCode);
        } else {
            mFragment.requestPermissions(new String[]{mPermission}, iRequestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == iRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionCallback.onPermissionResult(iRequestCode, PermissionEnum.GRANTED);
            } else {
                boolean isNeverAsk = isActivity
                        ? ActivityCompat.shouldShowRequestPermissionRationale(mActivity, mPermission)
                        : mFragment.shouldShowRequestPermissionRationale(mPermission);
                mPermissionCallback.onPermissionResult(iRequestCode, isNeverAsk
                        ? PermissionEnum.NEVER_ASK_AGAIN : PermissionEnum.DENIED);
            }
        }
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(mActivity, mPermission) == PackageManager.PERMISSION_GRANTED;
    }
}
