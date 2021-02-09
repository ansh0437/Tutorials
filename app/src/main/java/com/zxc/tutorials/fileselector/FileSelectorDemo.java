package com.zxc.tutorials.fileselector;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxc.tutorials.R;
import com.zxc.tutorials.permission.PermissionCallback;
import com.zxc.tutorials.permission.PermissionHelper;
import com.zxc.tutorials.utils.DialogUtil;

import java.io.File;

public class FileSelectorDemo extends AppCompatActivity {

    private static final String TAG = "File Selector";

    private final int PDF = 1;
    private final int EXCEL = 2;
    private int iFileType = 0;

    private int REQUEST_CODE_STORAGE = 1;

    private int iStorageCode = 13;
    private PermissionHelper mPermissionUtil;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector_demo);
        mTextView = findViewById(R.id.textView);
        mPermissionUtil = new PermissionHelper(this, mPermissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final PermissionCallback mPermissionCallback = new PermissionCallback() {
        @Override
        public void onGranted(int requestCode) {
            if (requestCode == REQUEST_CODE_STORAGE) {
                storageTask();
            }
        }

        @Override
        public void onDenied(int requestCode, boolean isNeverAskAgain) {
            if (requestCode == REQUEST_CODE_STORAGE) {
                if (isNeverAskAgain) {
                    mPermissionUtil.neverAskDialog();
                } else {
                    mPermissionUtil.askAgainDialog("Allow storage permission to select file from storage.");
                }
            }
        }
    };

    public void pdf(View view) {
        iFileType = PDF;
        mPermissionUtil.askPermission(REQUEST_CODE_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void excel(View view) {
        iFileType = EXCEL;
        mPermissionUtil.askPermission(REQUEST_CODE_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void storageTask() {
        String[] mimeTypes = new String[]{};
        if (iFileType == PDF) {
            mimeTypes = new String[]{"application/pdf"};
        } else if (iFileType == EXCEL) {
            mimeTypes = new String[]{"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Choose File"), iStorageCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == iStorageCode) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    try {
                        String path = FileUtils.getPath(this, fileUri);
                        File file = new File(path);
                        mTextView.setText(
                                String.format(
                                        "Uri: %s\n\n\nPath: %s",
                                        fileUri.toString(), file.getAbsolutePath()
                                )
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        mTextView.setText(String.format("Exception: %s", e.getMessage()));
                    }
                } else DialogUtil.alert(this, "Operation cancelled by user.");
            }
        }
    }
}