package com.app.bickup_user.track_driver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;


public class MapsLive extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_driver);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track_driver);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(28.544431, 77.389192);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_location_pin))
                .anchor(0.5f, 0.5f)
                .title("Noida");
         mMap.addMarker(markerOptions);
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));

        showLocationOnmap();


/*
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(final CameraPosition cameraPosition) {
                showMarkers(cameraPosition.target);
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                for (Map.Entry<Marker, Item> entry : markers) {
                    Marker marker = entry.getKey();
                    if (bounds.contains(marker.getPosition()) {
                        marker.setVisible(true);
                    } else {
                        marker.setVisible(false);
                    }
                }
            }
        });*/
    }

    /*private void setUpMap() {
// Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

// Add lots of markers to the map.
        addMarkersToMap();

// Setting an info window adapter allows us to change the both the
// contents and look of the
// info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

// Set listeners for marker events. See the bottom of this class for
// their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);

// Pan to see all markers in view.
// Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds.Builder bld = new LatLngBounds.Builder();
                    for (int i = 0; i < mAvailableCars.size(); i++) {
                        LatLng ll = new LatLng(Cars.get(i).getPos().getLat(), Cars.get(i).getPos().getLon());
                        bld.include(ll);
                    }
                    LatLngBounds bounds = bld.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
                    mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });
        }
    }*/




    public void showLocationOnmap() {

        String lattitude="28.544431";
        String longitude="77.389192";
        String droplattitude="28.630931";
        String dropLongitude="77.207600";

         prepareRouteUrl(Double.parseDouble(lattitude), Double.parseDouble(longitude), Double.parseDouble(droplattitude), Double.parseDouble(dropLongitude));

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
                                drawPath(String.valueOf(resultObject));
                                break;
                        }
                    }
                });
    }

    public void drawPath(String result){
        try {
            final JSONObject jsonObject = new JSONObject(result);

            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);


            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");

            String statusString = jsonObject.getString("status");

            Log.d("test: ", encodedString);
            List<LatLng> list = decodePoly(encodedString);

            LatLng last = null;
            for (int i = 0; i < list.size()-1; i++) {
                LatLng src = list.get(i);
                LatLng dest = list.get(i+1);
                last = dest;
                Log.d("Last latLng:", last.latitude + ", " + last.longitude );
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(4)
                        .color(Color.GREEN));
            }

            Log.d("Last latLng:", last.latitude + ", " + last.longitude );
        }catch (JSONException e){
            e.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "+ e.getMessage());
        }
    }

    private List<LatLng> decodePoly(String encoded){

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0;
        int length = encoded.length();

        int latitude = 0;
        int longitude = 0;

        while(index < length){
            int b;
            int shift = 0;
            int result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int destLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            latitude += destLat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b > 0x20);

            int destLong = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            longitude += destLong;

            poly.add(new LatLng((latitude / 1E5),(longitude / 1E5) ));
        }
        return poly;
    }
}
