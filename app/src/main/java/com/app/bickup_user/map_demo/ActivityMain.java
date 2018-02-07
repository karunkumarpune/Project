package com.app.bickup_user.map_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.app.bickup_user.map_demo.Constants.Drop_REQUEST_CODE;
import static com.app.bickup_user.map_demo.Constants.Pickup_REQUEST_CODE;
import static com.app.bickup_user.map_demo.Constants.is_check;

public class ActivityMain extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LatLng mCenterLatLong;
    private double current_latitude = 0.0;
    private double current_longitude = 0.0;
    public static String TAG = ActivityMain.class.getSimpleName();
    private Context mContext;




    private ImageView search_pickup,search_drop,btn_current_location;
    private TextView tv_pickup_location,tv_drop_location;
    private LatLng latLng;


    private String A_address,B_address;
    private double A_picup_latitude,A_drop_longitude;
    private double B_picup_latitude,B_drop_longitude;

    //Google Polyline
    private GoogleMap mMap;
    private ArrayList<Marker> markerList;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContext=this;

        initMap();
        tv_pickup_location = findViewById(R.id.tv_pickup_location);
        tv_drop_location = findViewById(R.id.tv_drop_location);
        btn_current_location = findViewById(R.id.btn_current_location);
        search_pickup = findViewById(R.id.search_pickup);
        search_drop = findViewById(R.id.search_drop);







        search_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_check = 1;
                startActivityForResult(new Intent(ActivityMain.this,
                        GetDetaisActivity.class), Pickup_REQUEST_CODE);
            }
        });


        search_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_check = 2;
                startActivityForResult(new Intent(ActivityMain.this,
                        GetDetaisActivity.class), Drop_REQUEST_CODE);

            }
        });

    }




    /*Data Result on Activity*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Pickup_REQUEST_CODE  && resultCode  == RESULT_OK) {
                Bundle bundle = data.getParcelableExtra("bundle");
                A_address= bundle.getString("Key");
                latLng= bundle.getParcelable("LatLng");
                tv_pickup_location.setText(A_address);
                A_picup_latitude=latLng.latitude;
                A_drop_longitude=latLng.longitude;
                showLocationOnmap();
            }

            if (requestCode == Drop_REQUEST_CODE  && resultCode  == RESULT_OK) {
                Bundle bundle = data.getParcelableExtra("bundle");
                B_address= bundle.getString("Key");
                latLng= bundle.getParcelable("LatLng");
                tv_drop_location.setText(B_address);
                B_picup_latitude=latLng.latitude;
                B_drop_longitude=latLng.longitude;
                showLocationOnmap();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        Log.d("Location","A ->>  "+ A_picup_latitude +":"+A_drop_longitude );
        Log.d("Location","B ->>  "+ B_picup_latitude +":"+B_drop_longitude );
    }



    /*Map init*/

    private void initMap(){
        btn_current_location = findViewById(R.id.btn_current_location);
        setGoogleMap();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {checkLocationPermission();} catch (Exception ignored) {}
                try {buildGoogleApiClient();mGoogleApiClient.connect();}
                catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

//-------------------------------Map--------------------------
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        //clearMap();
        latLng = new LatLng(current_latitude, current_longitude);
        A_address=getAddress(latLng);
        tv_pickup_location.setText(A_address);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_location))
                .anchor(0.5f, 0.5f)
                .title(A_address);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

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
            if (location != null)
            current_latitude = location.getLatitude();
            current_longitude = location.getLongitude();

            if(is_check==0) {
                clearMap();
                latLng = new LatLng(current_latitude, current_longitude);
                A_address = getAddress(latLng);
                tv_pickup_location.setText(A_address);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_location))
                        .anchor(0.5f, 0.5f)
                        .title(A_address);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            }



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

    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mAupportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.mMap = googleMap;
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
            this.mMap.setMapStyle(style);
            clearMap();
            if(is_check!=0) {
              showLocationOnmap();
           }
        }
    }

    /*Poly Line Googe Map...*/

    public void showLocationOnmap() {
        clearMap();
        markerList = new ArrayList<>();
        addMarkere(A_picup_latitude, A_drop_longitude, "", R.drawable.pin_location_pin);
        addMarkere(B_picup_latitude,B_drop_longitude, "", R.drawable.drop_location_pin);
        prepareRouteUrl(A_picup_latitude,A_drop_longitude ,B_picup_latitude,B_drop_longitude);
    }

    public void addMarkere(Double lattitude, Double longitude, String title, int marker) {
        LatLng sydney = new LatLng(lattitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(marker))
                .anchor(0.5f, 0.5f);
        //.title(title);
        Marker marker1 = mMap.addMarker(markerOptions);
        markerList.add(marker1);
    }

    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void showAllMarkers(Marker v, Marker parseDouble) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(v.getPosition().latitude, v.getPosition().longitude));
        builder.include(new LatLng(parseDouble.getPosition().latitude, parseDouble.getPosition().longitude));
        LatLngBounds bounds = builder.build();
        mMap.setPadding(200, 200, 100, 200);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(v.getPosition().latitude, v.getPosition().longitude)).zoom(15).tilt(60).bearing(90).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 10);
        mMap.moveCamera(cu);
    }

    public void prepareRouteUrl(double lattitude, double longitude, double droplattitude, double dropLongitude) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        url = url + "origin=" + lattitude + "," + longitude + "&destination=" + droplattitude + "," + dropLongitude + "&mode=driving&key=AIzaSyC-xQ2NJX_QoyLjZQJw8DWnJQwqnJvmTI4";
        callAPIForDrawRoute(url);
    }

    private void callAPIForDrawRoute(String url) {
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        switch (status) {
                            case 200:
                                new ParserTask().execute(String.valueOf(resultObject));
                                break;
                        }
                    }
                });
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.color(getApplication().getResources().getColor(R.color.appcolor));

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                Polyline polyline = mMap.addPolyline(lineOptions);
                polyline.setWidth(15);
                showAllMarkers(markerList.get(0), markerList.get(1));
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    public class DataParser {
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude));
                                hm.put("lng", Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }


            return routes;
        }
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }

}
