package com.app.bickup_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.controller.NetworkCallBack;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.model.User;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;


public class MainActivity extends AppCompatActivity implements
        InternetConnectionBroadcast.ConnectivityRecieverListener, View.OnClickListener,
        NetworkCallBack, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;

//-----------------------------------------Map-------------
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Context mContext;
    private TextView edt_pickup_location,edt_drop_location;
    private LatLng mCenterLatLong;
    private TextView btn_pickup,btn_drop;
    private ImageView imageMarker,btn_current_location;

    private int check_pin=0;

    private double current_latitude = 0.0;
    private double current_longitude = 0.0;

    private SharedPreferences pref_pickup;
    private SharedPreferences pref_drop;
    private String current_pickup_address;
    private String current_drop_address;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//---------------------------------------------------------------------------
    private CoordinatorLayout mCoordinatorLayout;
    public static String TAG = MainActivity.class.getSimpleName();
    private boolean mIsConnected;
    private Activity mActivityreference;
    private TextView txtSmalltravellerName, txtLargeTravellername, txtsmallCost, txtLargeCost;
    private Button btnSmallSubmit, btnLargeSubmit;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private DuoDrawerLayout drawerLayout;
    private ImageView navigationDrawer;
    private RelativeLayout imgPickupSearch;
    private RelativeLayout imgDropSearch;
    private TextView userName;
    private TextView useremail;
    private RoundedImageView userImage;
    private SharedPreferences sharedPreferences;
    public String longitude;
    private CircularProgressView circularProgressBar;
    private String message;
    private String smallPickupCost = "";
    private String largePickupCost = "";
    private String smallPickupDistance = "";
    private String largePickupDistance = "";
    private final int REQUEST_FARE_DETAILS = 1001;
    private Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_main);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed", "Refreshed token: " + refreshedToken);

        mActivityreference = MainActivity.this;
        GloableVariable.Tag_check_locaton_type=0;
        check_pin=0;


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

               try {
                 //  askLocationSettings();
               }catch (Exception e){}

                try {
                    checkLocationPermission();
                }catch (Exception e){}

                try {
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                }catch (Exception e){}

            }
        } catch (Exception e) {}

//-------------------------------Map--------------------------


        pref_pickup = getSharedPreferences("MyPickup", Context.MODE_PRIVATE);
        pref_drop = getSharedPreferences("MyDrop", Context.MODE_PRIVATE);

        intializeViews();
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

        edt_pickup_location = findViewById(R.id.edt_pickup_location);
        edt_drop_location = findViewById(R.id.edt_drop_location);
        btn_current_location = findViewById(R.id.btn_current_location);
        btn_pickup = findViewById(R.id.btn_pickup);
        btn_drop = findViewById(R.id.btn_drop);
        imageMarker = findViewById(R.id.imageMarker);



//-----------------------------------------------------------------


        GloableVariable.Tag_pickup_home_type = "2";
        GloableVariable.Tag_drop_home_type = "2";
        GloableVariable.Tag_drop_home_type = "1";
        setUserData();

        btn_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                //askLocationSettings();
                            }catch (Exception e){}

                            try {
                                checkLocationPermission();
                            }catch (Exception e){}


                            try {
                                getMyLocation();
                            }catch (Exception e){}

                            try {
                                buildGoogleApiClient();
                            }catch (Exception e){}
                        }
                    }
                } catch (Exception e) {}
            }
        });


//-----------------------------Pickup-----Click----------------------------------
        btn_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_pickup_Click();
                check_pin=1;
            }
        });

//-----------------------------Drop-----Click----------------------------------
        btn_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_drop_Click();
                check_pin=2;

            }
        });

    //--------------------------------end Drop-----------------------------------



    }

    private void btn_pickup_Click() {
        imageMarker.setImageResource(R.drawable.ic_pin_pickup);

        //Drop Location save.........
        SharedPreferences.Editor drop_edit = pref_drop.edit();
        String drop_lat=String.valueOf(mCenterLatLong.latitude);
        String drop_longs=String.valueOf(mCenterLatLong.longitude);
        String drop_address=current_drop_address;

        if(drop_lat !=null && drop_longs !=null && drop_address !=null) {
            drop_edit.putString("key_drop_lat", drop_lat);
            drop_edit.putString("key_drop_long",drop_longs);
            drop_edit.putString("key_drop_address",drop_address);
            drop_edit.apply();
        }


        //Pickup Get Location ..............
        String pickup_s=pref_pickup.getString("key_pickup_address","");
        String pickup_lat=pref_pickup.getString("key_pickup_lat","");
        String pickup_long=pref_pickup.getString("key_pickup_long","");
        if(pickup_s==null || pickup_s=="" && pickup_lat==null || pickup_lat==""  && pickup_long==null || pickup_long=="" ) {
            edt_pickup_location.setText("");
        }
        else {
            mMap.clear();
            GloableVariable.Tag_pickup_location_address = pickup_s;
            GloableVariable.Tag_pickup_latitude = Double.valueOf(pickup_lat.trim()).doubleValue();
            GloableVariable.Tag_pickup_longitude = Double.valueOf(pickup_long.trim()).doubleValue();
            edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
            LatLng latLng = new LatLng(Double.valueOf(pickup_lat.trim()).doubleValue(), Double.valueOf(pickup_long.trim()).doubleValue());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            current_pickup_address = onCameraPositionChanged_Pickup(mMap.getCameraPosition());
                            GloableVariable.Tag_pickup_location_address = current_pickup_address;
                            if (current_pickup_address == null) {
                                edt_pickup_location.setText("");
                            } else {
                                edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
                            }
                        }
                    });

                }
            });

    }

    private void btn_drop_Click() {
        imageMarker.setImageResource(R.drawable.ic_pin_drop);

        //Pickup Location save..............
        SharedPreferences.Editor p_edit = pref_pickup.edit();
        String p_lat=String.valueOf(mCenterLatLong.latitude);
        String p_longs=String.valueOf(mCenterLatLong.longitude);
        String p_address=current_pickup_address;

        if(p_lat !=null && p_longs !=null && p_address !=null) {
            p_edit.putString("key_pickup_lat",p_lat);
            p_edit.putString("key_pickup_long",p_longs);
            p_edit.putString("key_pickup_address",p_address);
            p_edit.commit();
        }

     //Drop Get Location ..............
        String drop_lat=pref_drop.getString("key_drop_lat","");
        String drop_long=pref_drop.getString("key_drop_long","");
        String drop_address=pref_drop.getString("key_drop_address","");

        if(drop_address==null || drop_address=="" && drop_lat==null || drop_lat==""  && drop_long==null || drop_long=="" ) {
            edt_drop_location.setText("");
        }
        else {
            GloableVariable.Tag_drop_location_address = drop_address;
            mMap.clear();
            edt_drop_location.setText(GloableVariable.Tag_drop_location_address);
            GloableVariable.Tag_drop_latitude = Double.valueOf(drop_lat.trim()).doubleValue();
            GloableVariable.Tag_drop_longitude = Double.valueOf(drop_long.trim()).doubleValue();
            LatLng latLng = new LatLng(Double.valueOf(drop_lat.trim()).doubleValue(), Double.valueOf(drop_long.trim()).doubleValue());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            current_drop_address=onCameraPositionChanged_Drop(mMap.getCameraPosition());
                            GloableVariable.Tag_drop_location_address=current_drop_address;
                            if(current_drop_address==null) {
                                edt_drop_location.setText("");
                            }else edt_drop_location.setText(GloableVariable.Tag_drop_location_address);
                        }});
                }});


    }


//-----------------------------Check Runtime Permissions--------------------

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
                        }catch (Exception e){}

                    }
                } else {
                    Toast.makeText(this, "Don't Permission denied ", Toast.LENGTH_LONG).show();
                }
                return;
            }
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

    //--------Ask----settings----------------
    private void askLocationSettings(){

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
        LatLng latLng = new LatLng(current_latitude,current_longitude);
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
            LatLng latLongs = new LatLng(mCenterLatLong.latitude,mCenterLatLong.longitude);
            String s=(getAddress(latLongs));
            if(s!=null){
                return s;
            }
            return  "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String onCameraPositionChanged_Drop(CameraPosition position) {
        mCenterLatLong = position.target;
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

    @Override
    protected void onResume() {
        super.onResume();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }



        edt_pickup_location = findViewById(R.id.edt_pickup_location);
        edt_drop_location = findViewById(R.id.edt_drop_location);


        int i= GloableVariable.Tag_check_locaton_type;
        if(i==0){

        }

        if(i==1){
            String pickup=GloableVariable.Tag_pickup_location_address;
            if(pickup !=null || pickup !="" ){
                edt_pickup_location.setText(pickup);
            }
            imageMarker.setImageResource(R.drawable.ic_pin_pickup);
            mMap.clear();
            LatLng latLng = new LatLng(GloableVariable.Tag_pickup_latitude,GloableVariable.Tag_pickup_longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
        if(i==2){
            String drop=GloableVariable.Tag_drop_location_address;
            if(drop !=null || drop !="" ){
                edt_drop_location.setText(drop);
            }
            imageMarker.setImageResource(R.drawable.ic_pin_drop);
            mMap.clear();
            LatLng latLng = new LatLng(GloableVariable.Tag_drop_latitude,GloableVariable.Tag_drop_longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }

        if(check_pin==1){
            btn_pickup_Click();
        }if (check_pin==2){
            btn_drop_Click();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        this.mMap.setMapStyle(style);

        mMap.setPadding(0,250,0,0);

        if(check_pin==0) {

            imageMarker.setImageResource(R.drawable.ic_pin_pickup);
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    current_pickup_address = onCameraPositionChanged_Pickup(mMap.getCameraPosition());
                    if (current_pickup_address == null) {
                        edt_pickup_location.setText("");
                    } else {
                        GloableVariable.Tag_pickup_location_address = current_pickup_address;
                        edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
                    }
                }
            });
        }

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
            LatLng latLongs = new LatLng(location.getLatitude(),location.getLongitude());
            String s=(getAddress(latLongs));
            if(s!=null){
                GloableVariable.Tag_pickup_location_address =s;
            }
            edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddress(LatLng location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String addresstxt = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (null != addresses && !addresses.isEmpty()) {
                addresstxt = "" + addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresstxt == null) {
            return addresstxt = "";
        }
        return addresstxt;
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
                current_latitude=location.getLatitude();
            current_longitude=location.getLongitude();
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

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void intializeViews() {
        navigationDrawer = findViewById(R.id.navigation_menu);
        navigationDrawer.setOnClickListener(this);
        circularProgressBar = findViewById(R.id.progress_view);
        findViewById(R.id.menu_delivery).setOnClickListener(this);
        findViewById(R.id.menu_scheduled).setOnClickListener(this);
        findViewById(R.id.menu_setting).setOnClickListener(this);
        findViewById(R.id.img_drawer_image).setOnClickListener(this);
        userName = findViewById(R.id.username);
        useremail = findViewById(R.id.userEmail);
        userImage = findViewById(R.id.img_drawer_image);
        userName.setText(User.getInstance().getFirstName()+" "+User.getInstance().getLastName() );
        useremail.setText(User.getInstance().getEmail());
        drawerLayout = findViewById(R.id.drawer_layout);

        mTypefaceRegular = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);

        RelativeLayout cardLargeTravellerContainer = findViewById(R.id.rl_large_traveller);
        RelativeLayout cardSmalltravellerContainer = findViewById(R.id.rl_small_traveller);
        cardSmalltravellerContainer.setOnClickListener(this);
        cardLargeTravellerContainer.setOnClickListener(this);

        txtSmalltravellerName = findViewById(R.id.txt_traveller_name_small);
        txtsmallCost = findViewById(R.id.txt_cost_small);
        txtLargeTravellername = findViewById(R.id.txt_traveller_name_large);
        txtLargeCost = findViewById(R.id.txt_cost_large);

        imgPickupSearch = findViewById(R.id.label_pickup_location_dialog);
        imgDropSearch = findViewById(R.id.label_drop_location);

        imgDropSearch.setOnClickListener(this);
        imgPickupSearch.setOnClickListener(this);

        btnLargeSubmit = findViewById(R.id.btn_submit_large);
        btnSmallSubmit = findViewById(R.id.btn_submit_small);

        btnLargeSubmit.setOnClickListener(this);
        btnSmallSubmit.setOnClickListener(this);

        //Set Font to Views
        txtLargeTravellername.setTypeface(mTypefaceBold);
        txtSmalltravellerName.setTypeface(mTypefaceBold);
        txtLargeCost.setTypeface(mTypefaceRegular);
        txtsmallCost.setTypeface(mTypefaceRegular);
        btnLargeSubmit.setTypeface(mTypefaceRegular);
        btnSmallSubmit.setTypeface(mTypefaceRegular);

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        TextView tv_header = toolbar.findViewById(R.id.txt_activty_header);
        toolbar.hideOverflowMenu();
        toolbar.showContextMenu();
        tv_header.setText(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.contactus_container).setOnClickListener(this);
        findViewById(R.id.aboutus_container).setOnClickListener(this);
        findViewById(R.id.privacy_container).setOnClickListener(this);
        findViewById(R.id.faq_container).setOnClickListener(this);
        findViewById(R.id.invite_and_earn_container).setOnClickListener(this);
        findViewById(R.id.change_password_container).setOnClickListener(this);
        findViewById(R.id.help_container).setOnClickListener(this);
    }



//-------Menu---Pic---Details---------------
    private void setUserData() {
        String firstName = User.getInstance().getFirstName();
        String lastName = User.getInstance().getLastName();
        String email = User.getInstance().getEmail();
        String mobile = User.getInstance().getMobileNumber();
        String userImageString = User.getInstance().getUserImage();



        GloableVariable.Tag_pickup_contact_name=firstName +" "+lastName;
        GloableVariable.Tag_pickup_contact_number=mobile;

        GloableVariable.Tag_drop_contact_name=firstName +" "+lastName;
        GloableVariable.Tag_drop_contact_number=mobile;



        if (userName != null && useremail != null) {
            userName.setText(User.getInstance().getFirstName() +" "+User.getInstance().getLastName());
            useremail.setText(User.getInstance().getEmail());
            if (userImage != null) {
                if (userImageString != null) {
                    Ion.with(userImage)
                            .placeholder(R.drawable.driver)
                            .error(R.drawable.driver)
                            .load(userImageString);
                }}}}

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage(getResources().getString(R.string.txt_close_app))
                .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor pic_edit = pref_drop.edit();
                        pic_edit.clear();
                        pic_edit.commit();

                        SharedPreferences.Editor pic_edit1 = pref_pickup.edit();
                        pic_edit1.clear();
                        pic_edit1.commit();

                        GloableVariable.Tag_pickup_latitude=0.0;
                        GloableVariable.Tag_pickup_longitude=0.0;

                        GloableVariable.Tag_drop_latitude=0.0;
                        GloableVariable.Tag_drop_latitude=0.0;

                        GloableVariable.Tag_pickup_location_address="";
                        GloableVariable.Tag_drop_location_address="";
                        dialog.dismiss();
                         callFinish();

                    }

                })
                .setNegativeButton(getResources().getString(R.string.txt_No), null)
                .show();

    }

    private void callFinish() {
        if(android.os.Build.VERSION.SDK_INT >= 21)
        {
            finishAndRemoveTask();
        }
        else
        {
            finish();
        }
    }

    private void checkInternetconnection() {
        mIsConnected = CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if (mIsConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }

    }

    public void showSnackBar(String mString) {}

//-----------Place Pikar Search-----------------------------
    private void openAutoComplePicker(int i) {
        if (i == 0) {
            //pickup
            try {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                        .setCountry("IN")
                        .build();
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .setFilter(typeFilter)
                                .setBoundsBias(new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)))
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
            } catch (GooglePlayServicesNotAvailableException e) {}
        }
        if (i == 1) {
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
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
            } catch (GooglePlayServicesRepairableException e) {
            } catch (GooglePlayServicesNotAvailableException e) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Pickap Location
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                GloableVariable.Tag_pickup_latitude = place.getLatLng().latitude;
                GloableVariable.Tag_pickup_longitude = place.getLatLng().longitude;

                SharedPreferences.Editor pic_edit1 = pref_pickup.edit();
                pic_edit1.clear();
                pic_edit1.commit();

                String s=(String) place.getAddress();
                if(s!=null){
                    GloableVariable.Tag_pickup_location_address=s;
                }
                startActivity(new Intent(this, PickupLocationActivity.class));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }//Drop Location
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                GloableVariable.Tag_drop_latitude = place.getLatLng().latitude;
                GloableVariable.Tag_drop_longitude = place.getLatLng().longitude;

                SharedPreferences.Editor pic_edit = pref_drop.edit();
                pic_edit.clear();
                pic_edit.commit();


                String s=(String) place.getAddress();
                if(s!=null){
                    GloableVariable.Tag_drop_location_address =s;
                }
                startActivity(new Intent(this, DropLocationActivity.class));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rl_small_traveller:
                prepareGetFareDetails(String.valueOf(GloableVariable.Tag_pickup_latitude),
                        String.valueOf(GloableVariable.Tag_pickup_longitude),
                        String.valueOf(GloableVariable.Tag_drop_latitude),
                        String.valueOf(GloableVariable.Tag_drop_longitude));
                setLayoutForSmallTraveller();
                break;
            case R.id.rl_large_traveller:
                prepareGetFareDetails(String.valueOf(GloableVariable.Tag_pickup_latitude),
                        String.valueOf(GloableVariable.Tag_pickup_longitude),
                        String.valueOf(GloableVariable.Tag_drop_latitude),
                        String.valueOf(GloableVariable.Tag_drop_longitude));

                setLayoutForLargetraveller();
                break;
            case R.id.btn_submit_small:
                showPopUp(1);
                break;
            case R.id.btn_submit_large:
                showPopUp(0);
                break;
            case R.id.navigation_menu:
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.menu_delivery:
                Intent intent = new Intent(this, DeliveryActivity.class);
                startActivity(intent);
                drawerLayout.isDrawerOpen(GravityCompat.START);
                break;
            case R.id.menu_scheduled:
                Intent scheDuled = new Intent(this, ScheduledActivity.class);
                startActivity(scheDuled);
                drawerLayout.isDrawerOpen(GravityCompat.START);
                break;
            case R.id.menu_setting:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);

                break;
            case R.id.label_pickup_location_dialog:
                check_pin=1;
                imageMarker.setImageResource(R.drawable.ic_pin_pickup);
                openAutoComplePicker(0);
                CommonMethods.getInstance().hideSoftKeyBoard(this);
                overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
                break;
            case R.id.label_drop_location:
                check_pin=2;
                imageMarker.setImageResource(R.drawable.ic_pin_drop);
                openAutoComplePicker(1);
                CommonMethods.getInstance().hideSoftKeyBoard(this);
                overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
                break;
            case R.id.img_drawer_image:
                Intent edit = new Intent(this, EditProfileActivity.class);
                startActivity(edit);
                break;
            case R.id.contactus_container:
                Intent cms = new Intent(this, CMSActivity.class);
                cms.putExtra(ConstantValues.CHOOSE_PAGE, 1);
                startActivity(cms);
                break;
            case R.id.aboutus_container:
                Intent cms1 = new Intent(this, CMSActivity.class);
                cms1.putExtra(ConstantValues.CHOOSE_PAGE, 3);
                startActivity(cms1);
                break;
            case R.id.privacy_container:
                Intent cms3 = new Intent(this, CMSActivity.class);
                cms3.putExtra(ConstantValues.CHOOSE_PAGE, 2);
                startActivity(cms3);
                break;
            case R.id.faq_container:
                Intent cms4 = new Intent(this, CMSActivity.class);
                cms4.putExtra(ConstantValues.CHOOSE_PAGE, 4);
                startActivity(cms4);
                break;
            case R.id.invite_and_earn_container:
                Intent cms5 = new Intent(this, CMSActivity.class);
                cms5.putExtra(ConstantValues.CHOOSE_PAGE, 5);
                startActivity(cms5);
                break;
            case R.id.change_password_container:
                Intent cms6 = new Intent(this, CMSActivity.class);
                cms6.putExtra(ConstantValues.CHOOSE_PAGE, 6);
                startActivity(cms6);
                break;
            case R.id.help_container:
                Intent cms7 = new Intent(this, CMSActivity.class);
                cms7.putExtra(ConstantValues.CHOOSE_PAGE, 7);
                startActivity(cms7);
                break;
        }

    }

//--------------------Booking Show Popup--------------------------
    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityreference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.pick_up_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView travellerName = openDialog.findViewById(R.id.txt_traveller_name_dialog);

        TextView travellerCost = openDialog.findViewById(R.id.txt_traveller_cost);
        TextView text_descrption = openDialog.findViewById(R.id.text_descrption);
        ImageView travellerImage = openDialog.findViewById(R.id.img_traveller);
        Button btnDisagree = openDialog.findViewById(R.id.btn_disagree);

        Button btnAgree = openDialog.findViewById(R.id.btn_agree);
        travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceRegular);
        btnAgree.setTypeface(mTypefaceRegular);
        if (choosetraveller == 1) {
            travellerName.setText(getResources().getString(R.string.txt_small_pickUP));
            travellerCost.setText("Fare Cost: $" + smallPickupCost);

            GloableVariable.Tag_total_price = smallPickupCost;
            GloableVariable.Tag_distance = smallPickupDistance;


        } else {

            travellerName.setText(getResources().getString(R.string.txt_medium_pickUP));
            travellerCost.setText("Fare Cost: $" + largePickupCost);

            GloableVariable.Tag_total_price = smallPickupCost;
            GloableVariable.Tag_distance = largePickupDistance;

        }
        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, GoodsActivity.class);
                startActivity(intent);
            }
        });
        openDialog.show();

    }

 //---------------------Card----Click---Booking-------------
    private void setLayoutForLargetraveller() {
        btnSmallSubmit.setVisibility(View.INVISIBLE);
        btnLargeSubmit.setVisibility(View.VISIBLE);

        txtLargeTravellername.setTextColor(getResources().getColor(R.color.grey_text_color));
        txtSmalltravellerName.setTextColor(getResources().getColor(R.color.greyColor));
        ImageView smallTraveller = findViewById(R.id.img_traveller_small);
        ImageView largeTraveller = findViewById(R.id.img_traveller_large);

        largeTraveller.setImageResource(R.drawable.ac_lg_vehicle_car);
        smallTraveller.setImageResource(R.drawable.de_sm_vehicle_car);

        RelativeLayout smallTravellers = findViewById(R.id.rl_small_traveller);
        RelativeLayout largeTravellers = findViewById(R.id.rl_large_traveller);


        LinearLayout.LayoutParams cardSmallTravelerLayoutParams = (LinearLayout.LayoutParams) smallTravellers.getLayoutParams();
        LinearLayout.LayoutParams cardLargeTravellerLayoutParams = (LinearLayout.LayoutParams) largeTravellers.getLayoutParams();

        cardLargeTravellerLayoutParams.setMargins(2, 0, 8, 0);
        cardSmallTravelerLayoutParams.setMargins(8, 20, 2, 0);
        smallTravellers.setLayoutParams(cardSmallTravelerLayoutParams);
        largeTravellers.setLayoutParams(cardLargeTravellerLayoutParams);


    }

    private void setLayoutForSmallTraveller() {
        btnSmallSubmit.setVisibility(View.VISIBLE);
        btnLargeSubmit.setVisibility(View.INVISIBLE);

        txtSmalltravellerName.setTextColor(getResources().getColor(R.color.grey_text_color));
        txtLargeTravellername.setTextColor(getResources().getColor(R.color.greyColor));
        ImageView smallTravellers = (ImageView) findViewById(R.id.img_traveller_small);
        ImageView largeTravellers = (ImageView) findViewById(R.id.img_traveller_large);


        largeTravellers.setImageResource(R.drawable.de_large_pickup);
        smallTravellers.setImageResource(R.drawable.ac_sm_vehicle_car);

        RelativeLayout smallTraveller = (RelativeLayout) findViewById(R.id.rl_small_traveller);
        RelativeLayout largeTraveller = (RelativeLayout) findViewById(R.id.rl_large_traveller);
        LinearLayout.LayoutParams cardLayoutParams = (LinearLayout.LayoutParams) smallTraveller.getLayoutParams();
        LinearLayout.LayoutParams cardLargeLayoutParams = (LinearLayout.LayoutParams) largeTraveller.getLayoutParams();

        cardLayoutParams.setMargins(2, 0, 8, 0);
        cardLargeLayoutParams.setMargins(8, 20, 2, 0);
        smallTraveller.setLayoutParams(cardLayoutParams);
        largeTraveller.setLayoutParams(cardLargeLayoutParams);
    }

    public void prepareGetFareDetails(String lattitude, String longitude, final String droplattitude, final String droplongitude) {
        String createUserUrl = WebAPIManager.getInstance().getFareDetailsUrl();
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty(ConstantValues.PICKUP_LATTITUDE, lattitude);
        requestBody.addProperty(ConstantValues.PICKUP_LONGITUDE, longitude);
        requestBody.addProperty(ConstantValues.DROP_LATITUDE, droplattitude);
        requestBody.addProperty(ConstantValues.DROP_LONGITUDE, droplongitude);
        getApproxfareDetails(requestBody, createUserUrl, this, 60 * 1000, REQUEST_FARE_DETAILS);
    }
//---------------------API--------Get-Fare---Details----------
    private void getApproxfareDetails(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("POST", createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value = String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            message = jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status) {
                            case 422:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                            case 202:
                                loginActivity.onSuccess(resultObject, requestCode, status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode) {
            case REQUEST_FARE_DETAILS:
                ParseFareDetailsResult parseFareDetailsResult = new ParseFareDetailsResult();
                parseFareDetailsResult.execute(data.toString());
                break;
        }
    }

    @Override
    public void onError(String msg) {
    }
    class ParseFareDetailsResult extends AsyncTask<String, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray data = jsonObject.getJSONArray("response");
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject smallTraveller = data.getJSONObject(0);
                JSONObject largeTraveller = data.getJSONObject(1);
                map.put(ConstantValues.CAR_NAME, smallTraveller.getString("car_name"));
                map.put(ConstantValues.CAR_TYPE, smallTraveller.getString("car_type"));
                map.put(ConstantValues.TOTAL_FARE, smallTraveller.getString("total_fare"));
                map.put(ConstantValues.TOTAL_Distance, smallTraveller.getString("distance"));

                map.put(ConstantValues.LARGE_CAR_NAME, largeTraveller.getString("car_name"));
                map.put(ConstantValues.LARGE_CAR_TYPE, largeTraveller.getString("car_type"));
                map.put(ConstantValues.LARGE_TOTAL_FARE, largeTraveller.getString("total_fare"));
                map.put(ConstantValues.LARGE_TOTAL_Distance, largeTraveller.getString("distance"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            setDataToViews(hashMap);
        }
    }

    private void setDataToViews(HashMap<String, String> hashMap) {
        //   smallPickupCost=hashMap.get(ConstantValues.TOTAL_FARE);
        // largePickupCost=hashMap.get(ConstantValues.LARGE_TOTAL_FARE);

        smallPickupDistance = hashMap.get(ConstantValues.TOTAL_Distance);
        largePickupDistance = hashMap.get(ConstantValues.LARGE_TOTAL_Distance);

        double value = 0.0;
        double values22 = 0.0;
        double value2 = 0.0;
        double values = 0.0;

        try {
            value = Double.parseDouble(hashMap.get(ConstantValues.TOTAL_FARE));
            values = Double.parseDouble(new DecimalFormat("#.##").format(value));
            //  Log.d("TAGS", String.valueOf(values));
        } catch (Exception e) {
        }

        try {

            value2 = Double.parseDouble(hashMap.get(ConstantValues.LARGE_TOTAL_FARE));
            values22 = Double.parseDouble(new DecimalFormat("#.##").format(value2));
            // Log.d("TAGS", String.valueOf(values22));}
        } catch (Exception e) {
        }

        smallPickupCost = "" + values;
        largePickupCost = "" + values22;
        txtsmallCost.setText("Fare Cost: $" + values);
        txtLargeCost.setText("Fare Cost: $" + values22);

    }


 //------------------------------------------------------------------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor pic_edit = pref_drop.edit();
        pic_edit.clear();
        pic_edit.commit();

        SharedPreferences.Editor pic_edit1 = pref_pickup.edit();
        pic_edit1.clear();
        pic_edit1.commit();

    }
}