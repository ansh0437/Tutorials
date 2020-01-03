package com.zxc.tutorials.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.zxc.tutorials.permission.PermissionCallback;
import com.zxc.tutorials.permission.PermissionHelper;

public class LocationHelper extends AppCompatActivity {

    private static final String TAG = "LocationHelper";

    private final int REQUEST_CODE_PLAY_SERVICES = 1;
    private final int REQUEST_CODE_LOCATION = 2;
    private final int REQUEST_CODE_GPS = 3;

    private LocationRequest mLocationRequest;
    private PermissionHelper mPermissionHelper;

    private void setup() {
        mPermissionHelper = new PermissionHelper(this, mPermissionCallback);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setMaxWaitTime(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdates() {
        setup();
        if (isGooglePlayServicesAvailable()) {
            if (mPermissionHelper.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                initGAPI();
            } else {
                locationPermissionNeeded();
            }
        } else {
            playServicesNotFound();
        }
    }

    public void playServicesNotFound() {

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(
                        this,
                        status,
                        REQUEST_CODE_PLAY_SERVICES
                ).show();
            }
            return false;
        }
        return true;
    }

    public void locationPermissionNeeded() {

    }

    public void askLocationPermission() {
        mPermissionHelper.askPermission(REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PermissionCallback mPermissionCallback = new PermissionCallback() {
        @Override
        public void onGranted(int requestCode) {
            if (requestCode == REQUEST_CODE_LOCATION) {
                initGAPI();
            }
        }

        @Override
        public void onDenied(int requestCode, boolean isNeverAskAgain) {
            if (requestCode == REQUEST_CODE_LOCATION) {
                if (isNeverAskAgain) {
                    locationPermissionNeverAsk();
                } else {
                    String MESSAGE_LOCATION_PERMISSION = "Location permission is required to get the current address.";
                    mPermissionHelper.askAgainDialog(MESSAGE_LOCATION_PERMISSION);
                }
            }
        }
    };

    public void locationPermissionNeverAsk() {
        // TODO: 02-01-2020 This means that user selected Don't Ask Again and denied the permission
    }

    private void initGAPI() {
        GoogleApiClient.Builder mBuilder = new GoogleApiClient.Builder(this);
        mBuilder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Log.e(TAG, "onConnected: ");
                checkGPS();
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.e(TAG, "onConnectionSuspended: " + i);
                googleFailure();
            }
        });
        mBuilder.addOnConnectionFailedListener(connectionResult -> {
            Log.e(TAG, "initGAPI: Google Api Client connection failed - " + connectionResult.getErrorMessage());
            googleFailure();
        });
        mBuilder.addApi(LocationServices.API);
        GoogleApiClient mGoogleApiClient = mBuilder.build();
        mGoogleApiClient.connect();
    }

    public void googleFailure() {

    }

    private void checkGPS() {
        LocationSettingsRequest.Builder mBuilder = new LocationSettingsRequest.Builder();
        mBuilder.addLocationRequest(mLocationRequest);
        mBuilder.setAlwaysShow(true);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(mBuilder.build())
                .addOnCompleteListener(task -> {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        gpsEnabled();
                    } catch (ApiException e) {
                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                try {
                                    resolvableApiException.startResolutionForResult(this, REQUEST_CODE_GPS);
                                } catch (IntentSender.SendIntentException ex) {
                                    ex.printStackTrace();
                                    gpsDisabled();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                gpsDisabled();
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GPS) {
            if (resultCode == Activity.RESULT_OK) gpsEnabled();
            else gpsDisabled();
        } else if (requestCode == REQUEST_CODE_PLAY_SERVICES) {
            if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Google Play Services must be installed", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void gpsEnabled() {
        locationUpdates();
    }

    public void gpsDisabled() {

    }

    private void locationUpdates() {
        LocationServices
                .getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    public void removeLocationUpdates() {
        LocationServices
                .getFusedLocationProviderClient(this)
                .removeLocationUpdates(mLocationCallback);
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            onLocationChanged(locationResult.getLastLocation());
        }
    };

    public void onLocationChanged(Location location) {

    }
}
