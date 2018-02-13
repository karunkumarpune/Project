package com.app.bickup_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.model.User;
import com.app.bickup_user.retrofit.APIService;
import com.app.bickup_user.retrofit.ApiUtils;
import com.app.bickup_user.tracking_status.MyAdapter;
import com.app.bickup_user.tracking_status.Status;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class TrackDriverActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener {

    public static String OPENTYPESGOODS="opentypesgoods";
    private RelativeLayout txtTrackStatus;
    private TextView txtHeaderText;
    private TextView txtDriverName;
    private TextView txtDriverTexiAddress;
    private TextView txtDriverNameBottomSheet;
    private TextView txtDriverTexiAddressBottomSheet;
    private ImageView imgDriverImage;
    private ImageView imgDriverImageBottomSheet;
    private ImageView callDriver;
    private ImageView callDriverBottomSheet;
 //   private Button btnAssignAnother;
    private Button btnAssignAnotherBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView imgBackButton;
    private RelativeLayout rlBottomSheet;
    private String message = "";


    private GoogleMap googleMap;
    private ArrayList<Marker> markerList;
    private Location mCurrentLocation;
    private boolean mLocationPermission = false;
    private LocationManager mLocationmanager;
    private LocationListener mLocationListener;


    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity activity;
    private CoordinatorLayout mCoordinatorLayout;
   // private CircularProgressView circularProgressView;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private  ImageView open_bottomSheet;
    private Socket socket;
    private final  static  String TAG="TrackDriverActivity";

    private RecyclerView recyclerView_status;
    private ArrayList<Status> list;
    private ArrayList<Status> list_uncheck;
    private MyAdapter myAdapter;
    private APIService mAPIService;
    private String ride_id;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_track_driver);
        mAPIService = ApiUtils.getAPIService();
        list=new ArrayList<>();
        list_uncheck=new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues.USER_PREFERENCES, Context.MODE_PRIVATE);
        accessToken=sharedPreferences.getString(ConstantValues.USER_ACCESS_TOKEN,"");

        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_booking_status)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_on_the_way)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_arrived)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_loading)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_Enroute)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_reached)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_unload)));
        list.add(new Status("1513322502922",this.getResources().getString(R.string.txt_delivered)));

        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_booking_status)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_on_the_way)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_arrived)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_loading)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_Enroute)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_reached)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_unload)));
        list_uncheck.add(new Status("1513322502922",this.getResources().getString(R.string.txt_delivered)));






        // TrackDriverActivity.this.connectSocketForLocationUpdates();
        setGoogleMap();
        initiTializeViews();


    }

    private void connectSocketForLocationUpdates() {
           socket.connect();
        try {
            socket = IO.socket(ConstantValues.BASE_URL);
            socket.connect();
            Log.d(TAG, "Socket ID : " + socket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
       // socket.disconnect();
    }

    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_track_driver);
        mAupportMapFragment.getMapAsync(this);

    }

    private void initiTializeViews() {
      //  mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_track_driver);
      //  circularProgressView= (CircularProgressView) findViewById(R.id.progress_view);
        recyclerView_status= findViewById(R.id.recyclerView_status);
        activity=this;
        mActivityreference=this;
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        txtHeaderText=(TextView)findViewById(R.id.txt_activty_header);
        txtHeaderText.setText(getString(R.string.txt_tracking_driver));
        txtTrackStatus=findViewById(R.id.txt_track_status);

        txtDriverName=(TextView)findViewById(R.id.txt_drver_name);
        open_bottomSheet= findViewById(R.id.open_bottomSheet);

        txtDriverNameBottomSheet=(TextView)findViewById(R.id.txt_drver_name_bottomsheet);
        txtDriverTexiAddress=(TextView)findViewById(R.id.txt_drver_address);
        txtDriverTexiAddressBottomSheet=(TextView)findViewById(R.id.txt_drver_address_bottomsheet);
       // btnAssignAnother=(Button)findViewById(btn_asign);
        btnAssignAnotherBottomSheet=(Button)findViewById(R.id.btn_asign_bottomsheet);
        imgDriverImageBottomSheet=(ImageView)findViewById(R.id.img_driver_bottomshet);
        imgDriverImage=(ImageView)findViewById(R.id.img_driver);
        callDriverBottomSheet=(ImageView)findViewById(R.id.img_call_bottomsheet);
        callDriver=(ImageView)findViewById(R.id.call_driver);
        ImageView imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        btnAssignAnotherBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetBehavior.setPeekHeight(0);

            }
        });

      // btnAssignAnother.setOnClickListener(this);

        rlBottomSheet=(RelativeLayout)findViewById(R.id.rl_bottomSheet);
        rlBottomSheet.setOnClickListener(this);


        txtHeaderText.setTypeface(mTypefaceRegular);
      //  txtTrackStatus.setTypeface(mTypefaceRegular);
        txtDriverName.setTypeface(mTypefaceRegular);
        txtDriverTexiAddress.setTypeface(mTypefaceRegular);
        txtDriverNameBottomSheet.setTypeface(mTypefaceRegular);
        txtDriverTexiAddressBottomSheet.setTypeface(mTypefaceRegular);
       // btnAssignAnother.setTypeface(mTypefaceRegular);
        btnAssignAnotherBottomSheet.setTypeface(mTypefaceRegular);

        setTypeFaceToViews();

        View bottomSheet = findViewById(R.id.design_bottom_sheet);


        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);

 mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if(newState==BottomSheetBehavior.STATE_COLLAPSED){
            //toolbarLyout.setVisibility(View.VISIBLE);
          //  mBottomSheetBehavior.setPeekHeight(200);
        }

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }

});


        txtTrackStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                       // open_bottomSheet.setImageResource(R.drawable.ic_expand);
                        break;
                    case MotionEvent.ACTION_UP:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                       // open_bottomSheet.setImageResource(R.drawable.ic_expand_less);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        txtTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   toolbarLyout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        recyclerView_status_LoadJson();
    }



    //Data Json Parse upldate status
    private void recyclerView_status_LoadJson() {
        /*String ride_ids="";
        if(ride_id!=null){
            ride_ids=ride_id;
        }*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_status.setLayoutManager(mLayoutManager);
        recyclerView_status.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new MyAdapter(list,list_uncheck);
        recyclerView_status.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();


     /*   final String[] message = new String[1];
       // circularProgressView.setVisibility(View.VISIBLE);
      mAPIService.getStatusUpdated(accessToken,ride_ids).enqueue(new Callback<OnGoing>() {

            @Override
            public void onResponse(Call<OnGoing> call, retrofit2.Response<OnGoing> response) {

              //  circularProgressView.setVisibility(View.GONE);
                int status = response.code();

                if (response.isSuccessful()) {
                    List<Responses> lists = response.body().getResponses();

                    for(int i=0;i<lists.size();i++){
                        Responses responses=lists.get(i);

                        for(Status status1:responses.getStatus()){
                            list.add(status1);
                        }
                        myAdapter = new MyAdapter(list,list_uncheck);
                        recyclerView_status.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }


                    try {
                       myAdapter = new MyAdapter(list,list_uncheck);
                        recyclerView_status.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }catch (Exception e){}

                }
                if (status != 200) {
                    switch (status) {
                        case 422:
                            Toast.makeText(TrackDriverActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(TrackDriverActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(TrackDriverActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 201:
                            Toast.makeText(TrackDriverActivity.this, getResources().getString(R.string.txt_driver_201), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TrackDriverActivity.this, message[0], Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<OnGoing> call, Throwable t) {
              //  circularProgressView.setVisibility(View.GONE);
                if (t != null) {
                    Toast.makeText(TrackDriverActivity.this, getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e(TAG, "Unable to submit post to API.");
            }
        });


*/
    }

    private void hidebottom() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("type")) {
                String type = extras.getString("type");
                if (type.equals("test type")) {
                    Toast.makeText(this, extras.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }



    private void showPopUp() {
        final Dialog openDialog = new Dialog(this);
       openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.assign_driver_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnAgree = (Button)openDialog.findViewById(R.id.btn_agree);
        Button btnDisAgree = (Button)openDialog.findViewById(R.id.btn_disagree);

        btnAgree.setTypeface(mTypefaceRegular);
        btnDisAgree.setTypeface(mTypefaceRegular);
        btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                Intent intent=new Intent(TrackDriverActivity.this,TypesGoods.class);
                intent.putExtra(OPENTYPESGOODS,1);
                startActivity(intent);
            }
        });
        btnDisAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

            }
        });
        openDialog.show();

    }
    private void setTypeFaceToViews() {
        TextView txtTrackStatusBottomSheet=(TextView)findViewById(R.id.track_status_bottomSheet);
        txtTrackStatusBottomSheet.setTypeface(mTypefaceRegular);
      //  TextView txtBookingStatusTime=(TextView)findViewById(R.id.txt_booking_status_time);
    //    TextView txtBookingStatus=(TextView)findViewById(R.id.txt_booking_status);
/*        TextView txtOnTheWayTime=(TextView)findViewById(R.id.txt_on_the_way_time);
        TextView txtOnTheWay=(TextView)findViewById(R.id.txt_on_the_way);
        TextView txtArrivedTime=(TextView)findViewById(R.id.txt_arrived_time);
        TextView txtArrived=(TextView)findViewById(R.id.txt_arrived);
        TextView txtLoadingTime=(TextView)findViewById(R.id.txt_loading_time);
        TextView txtLoading=(TextView)findViewById(R.id.txt_loading_time);
        TextView txtEnrouteTime=(TextView)findViewById(R.id.txt_enroute_time);
        TextView txtEnroute=(TextView)findViewById(R.id.txt_enroute);
        TextView txtReachedDropOff=(TextView)findViewById(R.id.txt_reached_drop_off);*/

      //  txtBookingStatusTime.setTypeface(mTypefaceRegular);
       // txtBookingStatus.setTypeface(mTypefaceRegular);
     /*   txtOnTheWayTime.setTypeface(mTypefaceRegular);
        txtOnTheWay.setTypeface(mTypefaceRegular);
        txtArrivedTime.setTypeface(mTypefaceRegular);
        txtArrived.setTypeface(mTypefaceRegular);
        txtLoading.setTypeface(mTypefaceRegular);
        txtLoadingTime.setTypeface(mTypefaceRegular);
        txtEnrouteTime.setTypeface(mTypefaceRegular);
        txtEnroute.setTypeface(mTypefaceRegular);
        txtReachedDropOff.setTypeface(mTypefaceRegular);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
            this.googleMap.setMapStyle(style);
            googleMap.clear();
            showLocationOnmap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ride_id=getIntent().getStringExtra("ride_id");
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
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

    public void showSnackBar(String mString) {
       /* snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
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

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.rl_bottomSheet:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetBehavior.setPeekHeight(0);
               // toolbarLyout.setVisibility(View.VISIBLE);
                break;
            case R.id.backImage_header:
              //  startActivity(new Intent(this,MainActivity.class));
               // finish();
                break;
            case R.id.btn_asign:
               showPopUp();
                break;
        }

    }


    public void showLocationOnmap() {

        GloableVariable.Tag_pickup_contact_name = User.getInstance().getFirstName() ;
        GloableVariable.Tag_pickup_contact_number = User.getInstance().getMobileNumber();

        GloableVariable.Tag_drop_contact_name = User.getInstance().getFirstName() ;
        GloableVariable.Tag_drop_contact_number = User.getInstance().getMobileNumber();

                clearMap();
                markerList = new ArrayList<>();

               double  lattitude= GloableVariable.Tag_pickup_latitude;
               double longitude= GloableVariable.Tag_pickup_longitude;

               double  droplattitude= GloableVariable.Tag_drop_latitude;
               double  dropLongitude= GloableVariable.Tag_drop_longitude;

                addMarkere(lattitude, longitude, "", R.drawable.pin_location_pin);
                addMarkere(droplattitude,dropLongitude, "", R.drawable.drop_location_pin);

                prepareRouteUrl(lattitude,longitude ,droplattitude,dropLongitude);
    }

    public void addMarkere(Double lattitude, Double longitude, String title, int marker) {
        LatLng sydney = new LatLng(lattitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(marker))
                .anchor(0.5f, 0.5f);
                //.title(title);
        Marker marker1 = googleMap.addMarker(markerOptions);
        markerList.add(marker1);
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
        googleMap.setPadding(200, 200, 200, 700);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(v.getPosition().latitude, v.getPosition().longitude)).zoom(5).tilt(90).bearing(50).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        googleMap.moveCamera(cu);
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
                lineOptions.color(TrackDriverActivity.this.getResources().getColor(R.color.appcolor));

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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  startActivity(new Intent(this,MainActivity.class));
       // finish();
    }
}
