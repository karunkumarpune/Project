package com.app.bickup_user.track_driver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.app.bickup_user.utility.ConstantValues;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackDriverMap extends AppCompatActivity implements OnMapReadyCallback {
    //https://maps.googleapis.com/maps/api/directions/json?origin=28.6355255,77.4450808&destination=29.467398,78.4227436&mode=driving
    private GoogleMap googleMap;
    private LocationManager mLocationmanager;
    private LocationListener mLocationListener;
    private CameraPosition mCameraPosition;
    private Location mCurrentLocation;
    private boolean mLocationPermission = false;


    public String lattitude;
    public String longitude;
    public String droplattitude;
    public String droplongitude;
    public static int RESULT_PICKUP = 200;
    public static int RESULT_DROP = 201;
    private ArrayList<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_driver);
        initializeForLocation(savedInstanceState);
        setGoogleMap();
    }

    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track_driver);
        mAupportMapFragment.getMapAsync(this);

    }
    private void initializeForLocation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(ConstantValues.KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(ConstantValues.KEY_CAMERA_POSITION);
        }
        mLocationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                updateLocationUI();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

    }
    private void getUserCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
            mCurrentLocation = mLocationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLocationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            updateLocationUI();
            return;
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style);
            this.googleMap.setMapStyle(style);
            googleMap.clear();
            showLocationOnmap();
            if (mLocationPermission) {
             getUserCurrentLocation();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        ConstantValues.PERMISSION_CURRENTLOCATION);
            }
        }
    }


    private void updateLocationUI() {
        if (googleMap != null) {
            if (mCurrentLocation != null) {
                LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                LatLng sydney = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 15);
                googleMap.animateCamera(myLocation);
                UiSettings mUiSetting = googleMap.getUiSettings();
                mUiSetting.setTiltGesturesEnabled(true);
                mUiSetting.setRotateGesturesEnabled(true);
            }
        }
    }

    public void addMarkere(Double lattitude, Double longitude, String title, int marker) {
        LatLng sydney = new LatLng(lattitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(marker))
                .anchor(0.5f, 0.5f)
                .title(title);
        Marker marker1 = googleMap.addMarker(markerOptions);
        markerList.add(marker1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermission = false;
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getUserCurrentLocation();
                    }
                     mLocationPermission = true;

                }
                break;
            case 222:
                if(grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        getUserCurrentLocation();
                    }
                }

        }
    }






    public void showLocationOnmap() {

       String lattitude="28.544431";
       String longitude="77.389192";
       String droplattitude="28.630931";
       String dropLongitude="77.207600";

                clearMap();
                markerList = new ArrayList<>();
                addMarkere(Double.parseDouble(lattitude), Double.parseDouble(longitude), "Pick", R.drawable.pin_location_pin);
                addMarkere(Double.parseDouble(droplattitude), Double.parseDouble(dropLongitude), "Drop", R.drawable.drop_location_pin);
                prepareRouteUrl(Double.parseDouble(lattitude), Double.parseDouble(longitude), Double.parseDouble(droplattitude), Double.parseDouble(dropLongitude));
                getUserCurrentLocation();
    }


    private void clearMap() {
        if (googleMap != null) {
            googleMap.clear();
        }
    }
    private void showAllMarkers(Marker v, Marker parseDouble) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(v.getPosition().latitude, v.getPosition().longitude));
        builder.include(new LatLng(parseDouble.getPosition().latitude, parseDouble.getPosition().longitude));
        LatLngBounds bounds = builder.build();
        googleMap.setPadding(100, 200, 100, 400);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(v.getPosition().latitude, v.getPosition().longitude)).zoom(15).tilt(60).bearing(90).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 10);
        googleMap.moveCamera(cu);

        // googleMap.setPadding(0, getResources().getDrawable(R.drawable.my_marker).getIntrinsicHeight(), 0, 0);
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
                                ParserTask parserTask = new ParserTask();
                                parserTask.execute(String.valueOf(resultObject));
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
                lineOptions.color(TrackDriverMap.this.getResources().getColor(R.color.appcolor));

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                Polyline polyline = googleMap.addPolyline(lineOptions);
                polyline.setWidth(20);
                showAllMarkers(markerList.get(0), markerList.get(1));
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    public class DataParser {

        /**
         * Receives a JSONObject and returns a list of lists containing latitude and longitude_pinmove
         */
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


        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
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


