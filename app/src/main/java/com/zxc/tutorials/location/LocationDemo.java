package com.zxc.tutorials.location;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxc.tutorials.R;
import com.zxc.tutorials.utils.DialogUtil;

public class LocationDemo extends LocationHelper {

    private static final String TAG = "Location Demo";

    private TextView mAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_demo);
        setTitle(TAG);
        mAddressTextView = findViewById(R.id.tvLocation);
    }

    public void locationClick(View view) {
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeLocationUpdates();
    }

    /* Overridden Methods From Base Activity */

    @Override
    public void playServicesNotFound() {
        super.playServicesNotFound();
        // TODO: 02-01-2020 Google Play Services not installed in phone
    }

    @Override
    public void locationPermissionNeeded() {
        super.locationPermissionNeeded();
        // TODO: 02-01-2020 Ask for location permission
        askLocationPermission();
    }

    @Override
    public void locationPermissionNeverAsk() {
        super.locationPermissionNeverAsk();
        // TODO: 02-01-2020 This means that user selected Don't Ask Again and denied the permission
    }

    @Override
    public void googleFailure() {
        super.googleFailure();
    }

    @Override
    public void gpsEnabled() {
        super.gpsEnabled();
    }

    @Override
    public void gpsDisabled() {
        super.gpsDisabled();
        DialogUtil.alert(this, "Gps is disabled");
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        String address = LocationUtil.getAddress(this, location);
        mAddressTextView.setText(address);
    }
}