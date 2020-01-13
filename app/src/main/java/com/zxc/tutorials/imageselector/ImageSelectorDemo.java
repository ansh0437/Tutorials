package com.zxc.tutorials.imageselector;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.zxc.tutorials.R;
import com.zxc.tutorials.permission.PermissionCallback;
import com.zxc.tutorials.permission.PermissionHelper;
import com.zxc.tutorials.utils.DialogUtil;

import java.io.File;

public class ImageSelectorDemo extends AppCompatActivity {

    private static final String TAG = "Image Selector";

    private int REQUEST_CODE_CAMERA = 1;
    private int REQUEST_CODE_GALLERY = 2;
    private PermissionHelper mPermissionUtil;

    private int iCameraCode = 11;
    private int iGalleryCode = 12;
    private String mSelectedFileName = "selectedPicture.jpeg";
    private File mSelectedImageFile;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector_demo);
        setTitle(TAG);
        mImageView = findViewById(R.id.imageView);
        mPermissionUtil = new PermissionHelper(this, mPermissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PermissionCallback mPermissionCallback = new PermissionCallback() {
        @Override
        public void onGranted(int requestCode) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                cameraTask();
            } else if (requestCode == REQUEST_CODE_GALLERY) {
                galleryTask();
            }
        }

        @Override
        public void onDenied(int requestCode, boolean isNeverAskAgain) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (isNeverAskAgain) {
                    mPermissionUtil.neverAskDialog();
                } else {
                    mPermissionUtil.askAgainDialog("Allow camera access to capture picture.");
                }
            } else if (requestCode == REQUEST_CODE_GALLERY) {
                if (isNeverAskAgain) {
                    mPermissionUtil.neverAskDialog();
                } else {
                    mPermissionUtil.askAgainDialog("Allow storage permission to select picture from gallery.");
                }
            }
        }
    };

    public void camera(View view) {
        mPermissionUtil.askPermission(REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
    }

    public void gallery(View view) {
        mPermissionUtil.askPermission(REQUEST_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void cameraTask() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            mSelectedImageFile = new File(getFilesDir(), mSelectedFileName);
            Uri photoURI = FileProvider.getUriForFile(
                    this, getPackageName() + ".provider",
                    mSelectedImageFile
            );
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(pictureIntent, iCameraCode);
        } else {
            DialogUtil.alert(this, "No app found to capture image.");
        }
    }

    private void galleryTask() {
        String[] mimeTypes = new String[]{"image/jpg", "image/jpeg", "image/png"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), iGalleryCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == iCameraCode) {
                Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImageFile.getAbsolutePath());
                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == iGalleryCode) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    try {
                        Bitmap bitmap;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            assert fileUri != null;
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), fileUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        }
                        mImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else DialogUtil.alert(this, "Operation cancelled by user.");
            }
        }
    }
}
