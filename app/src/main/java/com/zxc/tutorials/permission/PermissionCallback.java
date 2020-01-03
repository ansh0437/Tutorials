package com.zxc.tutorials.permission;

public interface PermissionCallback {

    void onGranted(int requestCode);

    void onDenied(int requestCode, boolean isNeverAskAgain);

}
