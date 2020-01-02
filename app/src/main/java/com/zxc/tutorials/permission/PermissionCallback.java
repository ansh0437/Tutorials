package com.zxc.tutorials.permission;

public interface PermissionCallback {
    void onPermissionResult(int requestCode, PermissionEnum permissionResult);
}
