package com.rsi.homemaid;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bean.Splash;
import helper.MarshMallowPermission;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;
import tools.HelperMethods;

// Test commit next
public class SplashActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PERMISSION_ALL = 10;
    private MarshMallowPermission marshMallowPermission;
    ;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        marshMallowPermission = new MarshMallowPermission(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!marshMallowPermission.hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
            return;
        }
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
           // if (AppSharedPreference.getInstance(SplashActivity.this).getIsLogin())  //TODO: Need to be remove
             //   startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            //else
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Call<Splash> call = apiService.getDeviceDetails(getDeviceDetailsJson(HelperMethods.getDeviceId(SplashActivity.this), "155@sd!$%he^ru#y", HelperMethods.getCurrentDateTime(), String.valueOf(currentLatitude), String.valueOf(currentLongitude)).toString());
            call.enqueue(new Callback<Splash>() {
                @Override
                public void onResponse(Call<Splash> call, Response<Splash> response) {
//                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                  //  if (AppSharedPreference.getInstance(SplashActivity.this).getIsLogin()) {

                    //    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    //} else {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    //}
                    finish();
                }
//                }

                @Override
                public void onFailure(Call<Splash> call, Throwable t) {
                    System.out.print(call);
                  //  if (AppSharedPreference.getInstance(SplashActivity.this).getIsLogin()) {//TODO: Need to be remove
                  //      startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                   // } else {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                  //  }
                    finish();
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            // Start an Activity that tries to resolve the error
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                            .setFastestInterval(1 * 1000); // 1 second, in milliseconds

                } else {

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private JSONArray getDeviceDetailsJson(String deviceId, String appId, String dateTime, String latitude, String longitude) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            paramObject.put("deviceId", deviceId);
            paramObject.put("applicationId", appId);
            paramObject.put("dateTime", dateTime);
            paramObject.put("latitude", latitude);
            paramObject.put("longitude", longitude);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private void calculateHashKey(String yourPackageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    yourPackageName,
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
