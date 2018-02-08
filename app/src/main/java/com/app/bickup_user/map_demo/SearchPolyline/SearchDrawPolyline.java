package com.app.bickup_user.map_demo.SearchPolyline;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

public class SearchDrawPolyline extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int REQUEST_LOCATION = 1;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private LocationManager mLocationManager;
    private LatLng from_latLng,to_latLng;
    private ArrayList<Marker> markerList;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                from_latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapMaker("");
                Log.d("Location", location.getLatitude() + "" + location.getLongitude());
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_polyline);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            getCurrentLocation();
        }

    }

    //Runtime permisstion
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT);
                // Permission was denied or request was cancelled
            }
        }
    }

    //Map ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.mMap = googleMap;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
      //  mMap.setMyLocationEnabled(true);
    }


    //Fron Search location.........
    public void btn_from(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.edt_from_location);
        String location = locationSearch.getText().toString();

        if (location.length() != 0) {

            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addressList != null;
            Address address = addressList.get(0);
            from_latLng = new LatLng(address.getLatitude(), address.getLongitude());
            MapMaker(address.getAddressLine(0));

            try {
                showLocationOnmap();
            }catch (Exception e){}

        } else
            Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_SHORT);
    }

    //To Search location
    public void btn_to(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.edt_to_location);
        String location = locationSearch.getText().toString();

        if (location.length() != 0) {

            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addressList != null;
            Address address = addressList.get(0);
            to_latLng = new LatLng(address.getLatitude(), address.getLongitude());

            MapMaker(address.getAddressLine(0));
            try {
                showLocationOnmap();
            }catch (Exception e){}

        } else
            Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_SHORT);
    }


    //Current Location.................
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(getApplicationContext(), "Please location provider", Toast.LENGTH_SHORT);
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("getCurrentLng", "" + location.getLatitude() + " : " + location.getLongitude());
            from_latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // MapMaker("");
        }
    }

    private void MapMaker(String address) {
        mMap.addMarker(new MarkerOptions()
                .position(from_latLng)
                .title("Address")
                .snippet(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_location_pin)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(from_latLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }



     /*Poly Line Googe Map...*/

    public void showLocationOnmap() {
        clearMap();
        markerList = new ArrayList<>();

        addMarkere(from_latLng.latitude, from_latLng.longitude, "", R.drawable.pin_location_pin);
        addMarkere(to_latLng.latitude,to_latLng.longitude, "", R.drawable.drop_location_pin);
        prepareRouteUrl(from_latLng.latitude, from_latLng.longitude,to_latLng.latitude,to_latLng.longitude);
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
        mMap.setPadding(10, 110, 10, 110);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(v.getPosition().latitude, v.getPosition().longitude)).zoom(11).tilt(40).bearing(50).build();
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