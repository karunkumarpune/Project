package com.app.bickup_user.curent_date;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.bickup_user.GPSTracker;
import com.app.bickup_user.R;

public class MainActivity extends AppCompatActivity {

    private boolean mLocationPermission;
    private static final int PERMISSION_REQUEST_CODE = 987;
    private GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gps = new GPSTracker(MainActivity.this);
                if(gps.canGetLocation()) {
                    Log.e("MainActivity", "Latitude: " + gps.getLatitude());
                    Log.e("MainActivity", "Longitude: " + gps.getLongitude());
                }
            }
        }


    }



   /* private void getUserCurrentLocation() {
        if (this.checkLocationPermission()) {
            gps = new GPSTracker(this);
            if(gps.canGetLocation()) {
                Log.e("MainActivity", "Latitude k: " + gps.getLatitude());
                Log.e("MainActivity", "Longitude n: " + gps.getLongitude());
            }

        } else {
            String[] permissionsArray = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissionsArray, PERMISSION_REQUEST_CODE);
        }

    }*/


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location")
                        .setMessage("Allow Bickup to access your device's location?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);}
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        gps = new GPSTracker(MainActivity.this);
                        if(gps.canGetLocation()) {
                            Log.e("MainActivity", "Latitude k: " + gps.getLatitude());
                            Log.e("MainActivity", "Longitude n: " + gps.getLongitude());
                        }}} else {}
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



    }
}
