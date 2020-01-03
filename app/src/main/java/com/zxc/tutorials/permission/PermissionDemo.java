package com.zxc.tutorials.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.zxc.tutorials.R;

public class PermissionDemo extends AppCompatActivity {

    private static final String TAG = "Permission Demo";

    private int REQUEST_CODE_CAMERA = 1;
    private PermissionHelper mPermissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_demo);
        setTitle(TAG);
        mPermissionUtil = new PermissionHelper(this, mPermissionCallback);
    }

    public void askPermission(View view) {
        mPermissionUtil.askPermission(REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PermissionCallback mPermissionCallback = new PermissionCallback() {
        @Override
        public void onGranted(int requestCode) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Toast.makeText(PermissionDemo.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDenied(int requestCode, boolean isNeverAskAgain) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (isNeverAskAgain) {
                    mPermissionUtil.neverAskDialog();
                } else {
                    mPermissionUtil.askAgainDialog("App needs camera permission to work properly.");
                }
            }
        }
    };
}
