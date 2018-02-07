package com.app.bickup_user.map_demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.app.bickup_user.map_demo.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static com.app.bickup_user.map_demo.Constants.is_check;

public class GetDetaisActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    //-----------------------------------------Map-------------
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LatLng mCenterLatLong;
    private ImageView imageMarker, btn_current_location,imageView123;
    private double current_latitude = 0.0;
    private double current_longitude = 0.0;
    public static String TAG = GetDetaisActivity.class.getSimpleName();
    private ScrollView scrollview;


    private ImageView search_address;
    private TextView tv_location_address,toolbar_text;
    private ImageButton btn_back,btn_ok;
    private String address;
    private double lattitude;
    private double longitude;
    private LatLng latLng;
    private Context mContext;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmap);
        mContext=this;

        initMap();
        btn_back = findViewById(R.id.btn_back);
        btn_ok = findViewById(R.id.btn_ok);

        search_address = findViewById(R.id.search_address);
        tv_location_address = findViewById(R.id.tv_location_address);
        toolbar_text = findViewById(R.id.toolbar_text);
        if(is_check==1)
            toolbar_text.setText("Pickup Location");
        else  toolbar_text.setText("Drop Location");



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = getIntent();
                Bundle args = new Bundle();
                args.putString("Key",address);
                args.putParcelable("LatLng", latLng);
                intent.putExtra("bundle",args);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutoComplePicker();
            }
        });


    }


    /*Open Place pikar */
    private void openAutoComplePicker() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .setCountry("IN")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .setBoundsBias(new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }


    /*Place Pickar getLocation Address..*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                address= ""+place.getAddress();
                if(address.contains(",")) {
                    String[] array = address.split(",", 3);
                    try {
                        address = array[0] + "," + array[1] + "," + array[2];
                    }catch (Exception e){}
                }

                tv_location_address.setText(address);
                latLng= place.getLatLng();
                lattitude=latLng.latitude;
                longitude=latLng.longitude;


                if(is_check==1) {
                    imageMarker.setImageResource(R.drawable.ic_pin_pickup);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }else {
                    imageMarker.setImageResource(R.drawable.ic_pin_drop);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }
             } //else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {}
               //else if (resultCode == RESULT_CANCELED) {}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


    /*Map init*/

    private void initMap(){

        imageMarker = findViewById(R.id.imageMarker);
        btn_current_location = findViewById(R.id.btn_current_location);
        scrollview = findViewById(R.id.scrollview);
        imageView123 = findViewById(R.id.imageView123);


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {checkLocationPermission();} catch (Exception ignored) {}
                try {buildGoogleApiClient();mGoogleApiClient.connect();}
                catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

//-------------------------------Map--------------------------
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_loication);
        mapFragment.getMapAsync(this);

        imageView123.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollview.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollview.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollview.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        btn_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            try {checkLocationPermission();} catch (Exception e) {}
                            try {getMyLocation();}catch (Exception e) {}
                            try {buildGoogleApiClient();} catch (Exception e) {}
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    /*Google Map Ready*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        this.mMap.setMapStyle(style);
        mMap.setPadding(0, 250, 0, 0);

        if(is_check==1) {
            imageMarker.setImageResource(R.drawable.ic_pin_pickup);
            currentMoveMap();
        }else {
            imageMarker.setImageResource(R.drawable.ic_pin_drop);
            currentMoveMap();
        }
    }


    private void currentMoveMap(){
        mMap.clear();
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                         address= onCameraPositionChanged(mMap.getCameraPosition());
                         tv_location_address.setText(""+address);
                    }
                });

            }
        });
    }


    private String onCameraPositionChanged(CameraPosition position) {
        mCenterLatLong = position.target;
        latLng = position.target;
        mMap.clear();
        try {
            Location mLocation = new Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            LatLng latLongs = new LatLng(mCenterLatLong.latitude,mCenterLatLong.longitude);
            String s=(getAddress(latLongs));
            if(s!=null){
                return s;
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------start---Check Runtime Permissions--------------------
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(false);
                        try {
                            buildGoogleApiClient();
                            mGoogleApiClient.connect();
                        } catch (Exception e) {
                        }

                    }
                } else {
                    Toast.makeText(this, "Don't Permission denied ", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //--------------------------end---Check Runtime Permissions--------------------
//--------Ask----settings----------------
    private void askLocationSettings() {

        if (checkPlayServices()) {
            if (!isLocationEnabled(mContext)) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    //----------getCurrent Location-------------------------
    private void getMyLocation() {
        mMap.clear();
        latLng = new LatLng(current_latitude, current_longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        mMap.animateCamera(cameraUpdate);
    }


    private String onCameraPositionChanged_Pickup(CameraPosition position) {
        mCenterLatLong = position.target;
        mMap.clear();
        try {
            Location mLocation = new Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            LatLng latLongs = new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude);
            String s = (getAddress(latLongs));
            if (s != null) {
                return s;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeMap(Location location) {
        Log.d(TAG, "Reaching map" + mMap);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;
            latLong = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.0f));
            LatLng latLongs = new LatLng(location.getLatitude(), location.getLongitude());
            String s = (getAddress(latLongs));
            if (s != null) {
                GloableVariable.Tag_pickup_location_address = s;
            }
            //  edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddress(LatLng location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (null != addresses && !addresses.isEmpty()) {
                address = "" + addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        //  mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            LatLng latLng;
            if (location != null)
                current_latitude = location.getLatitude();
            current_longitude = location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            //  changeMap(location);

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }





}
