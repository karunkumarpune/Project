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
import android.support.annotation.NonNull;
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
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;

import static com.app.bickup_user.GlobleVariable.GloableVariable.Tag_drop_location_address;
import static com.app.bickup_user.GlobleVariable.GloableVariable.Tag_pickup_location_address;
import static com.app.bickup_user.GlobleVariable.GloableVariable.is_check_pickup_or_drop;


public class MainActivity extends AppCompatActivity implements
        InternetConnectionBroadcast.ConnectivityRecieverListener, View.OnClickListener,
        NetworkCallBack, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


//-----------------------------------------Map-------------
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Context mContext;
    private TextView edt_pickup_location,edt_drop_location;

    private ImageView btn_current_location;
    private double current_latitude = 0.0;
    private double current_longitude = 0.0;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//---------------------------------------------------------------------------
    public static String TAG = MainActivity.class.getSimpleName();
    private boolean mIsConnected;
    private Activity mActivityreference;
    private TextView txtSmalltravellerName, txtLargeTravellername, txtsmallCost, txtLargeCost;
    private Button btnSmallSubmit, btnLargeSubmit;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private DuoDrawerLayout drawerLayout;
    private ImageView navigationDrawer;
    private TextView userName;
    private TextView useremail;
    private RoundedImageView userImage;
    public String longitude;
    private CircularProgressView circularProgressBar;
    private String message;
    private String smallPickupCost = "";
    private String largePickupCost = "";
    private String smallPickupDistance = "";
    private String largePickupDistance = "";
    private final int REQUEST_FARE_DETAILS = 1001;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;

    //Google Polyline
    private GoogleMap mMap;
    private ArrayList<Marker> markerList;
    private LatLng latLng;
    private String A_address;



    private SharedPreferences pref_pickup;
    private SharedPreferences pref_drop;

    private double pickup_latitude,pickup_longitude;
    private String pickup_location_address;

    private double drop_latitude,drop_longitude;
    private String drop_location_address;

    private RelativeLayout liner_btn_pickup,liner_btn_drop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_main);

        mActivityreference = MainActivity.this;
        pref_pickup = getSharedPreferences("MyPickup", Context.MODE_PRIVATE);
        pref_drop = getSharedPreferences("MyDrop", Context.MODE_PRIVATE);




        edt_pickup_location = findViewById(R.id.tv_pickup_location);
        edt_drop_location = findViewById(R.id.tv_drop_location);


        liner_btn_pickup = findViewById(R.id.label_pickup_locations);
        liner_btn_drop = findViewById(R.id.liner_btn_drops);




        initMap();
        intializeViews();
        setUserData();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
            String name=User.getInstance().getFirstName() +" "+User.getInstance().getLastName();
            userName.setText(name);
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







                        GloableVariable.is_check_pickup_or_drop=0;
                        GloableVariable.Tag_pickup_latitude=0.0;
                        GloableVariable.Tag_pickup_longitude=0.0;

                        GloableVariable.Tag_drop_latitude=0.0;
                        GloableVariable.Tag_drop_latitude=0.0;

                        Tag_pickup_location_address="";
                        Tag_drop_location_address="";
                        dialog.dismiss();
                        callFinish();

                    }

                })
                .setNegativeButton(getResources().getString(R.string.txt_No), null)
                .show();

    }

    private void callFinish() {
        if(android.os.Build.VERSION.SDK_INT >= 21)
        finishAndRemoveTask();
        else finish();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String pickup=edt_pickup_location.getText().toString();
        String drop=edt_drop_location.getText().toString();
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
            if(pickup.isEmpty()){
                buildDialog(R.style.DialogAnimation, "Please choose Pickup Location");
            }else if (drop.isEmpty()){
                buildDialog(R.style.DialogAnimation, "Please choose drop Location");
            }else
                showPopUp(1);
                break;
            case R.id.btn_submit_large:
                if(pickup.isEmpty()){
                    buildDialog(R.style.DialogAnimation, "Please choose Pickup Location");
                }else if (drop.isEmpty()){
                    buildDialog(R.style.DialogAnimation, "Please choose drop Location");
                }else
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
           /* case R.id.label_pickup_location_dialog:
                CommonMethods.getInstance().hideSoftKeyBoard(this);
                overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
                break;
            case R.id.label_drop_location:
                startActivity(new Intent(this, DropLocationActivity.class));
                CommonMethods.getInstance().hideSoftKeyBoard(this);
                overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
                break;
       */     case R.id.img_drawer_image:
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

    private void buildDialog(int animationSource, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(type);
        builder.setNegativeButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();
    }

   /*

    What is multithreading?

    What is the difference between Process and Thread?

   What are the benefits of multi-threaded programming?

   Can we call run() method of a Thread class?

   Why thread communication methods wait(), notify() and notifyAll() are in Object class?

   Why wait(), notify() and notifyAll() methods have to be called from synchronized method or block?

  What is difference between user Thread and daemon Thread?

  Which is more preferred – Synchronized method or Synchronized block?
*/

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            try {checkLocationPermission();} catch (Exception ignored) {}
                            try {getMyLocation();}catch (Exception ignored) {}
                            try {buildGoogleApiClient();} catch (Exception ignored) {}
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });




        liner_btn_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupSend();
            }
        });

        liner_btn_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DropSend();
            }
        });



        edt_pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupSend();
            }
        });

        edt_drop_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DropSend();
            }
        });


      /*
        search_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupSend();
            }
        });


        search_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DropSend();
            }
        });
*/




    }

    private void PickupSend(){
        startActivity(new Intent(MainActivity.this, PickupLocationActivity.class));

        //Pickup Location save..............
        SharedPreferences.Editor p_edit = pref_pickup.edit();
        p_edit.putString("key_pickup_lat", "" + pickup_latitude);
        p_edit.putString("key_pickup_long", "" + pickup_longitude);
        p_edit.putString("key_pickup_address", pickup_location_address);
        p_edit.apply();

        CommonMethods.getInstance().hideSoftKeyBoard(MainActivity.this);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);

    }

    private void DropSend(){
        startActivity(new Intent(MainActivity.this, DropLocationActivity.class));

        //Drop Location save..............
        SharedPreferences.Editor p_edit = pref_drop.edit();
        p_edit.putString("key_drop_lat", "" + drop_latitude);
        p_edit.putString("key_drop_long", "" + drop_longitude);
        p_edit.putString("key_drop_address", drop_location_address);
        p_edit.apply();

        CommonMethods.getInstance().hideSoftKeyBoard(MainActivity.this);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);


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
                        if(mMap !=null) {
                            mMap.setMyLocationEnabled(false);
                        }
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
        clearMap();
        latLng = new LatLng(current_latitude, current_longitude);
        A_address=getAddress(latLng);
        edt_pickup_location.setText(A_address);
        Tag_pickup_location_address=A_address;
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mAupportMapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        edt_drop_location = findViewById(R.id.tv_drop_location);
        edt_pickup_location = findViewById(R.id.tv_pickup_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onResume() {
        super.onResume();


        //Pickup...................
        pickup_latitude= Double.parseDouble(pref_pickup.getString("key_pickup_lat","1.2"));
        pickup_longitude= Double.parseDouble(pref_pickup.getString("key_pickup_long","1.2"));
        pickup_location_address=pref_pickup.getString("key_pickup_address","");
        edt_pickup_location.setText(pickup_location_address);

        GloableVariable.Tag_pickup_latitude=pickup_latitude;
        GloableVariable.Tag_pickup_longitude=pickup_longitude;
        GloableVariable.Tag_pickup_location_address=pickup_location_address;


        //Drop.....................
        drop_latitude= Double.parseDouble(pref_drop.getString("key_drop_lat","1.2"));
        drop_longitude= Double.parseDouble(pref_drop.getString("key_drop_long","1.2"));
        drop_location_address=pref_drop.getString("key_drop_address","");

        GloableVariable.Tag_drop_latitude=drop_latitude;
        GloableVariable.Tag_drop_longitude=drop_longitude;
        GloableVariable.Tag_drop_location_address=drop_location_address;


        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }


        if (is_check_pickup_or_drop == 1) {
            edt_pickup_location.setText(pickup_location_address);
        }
        if (is_check_pickup_or_drop == 2) {
            edt_drop_location.setText(drop_location_address);
        }
        if (is_check_pickup_or_drop != 0) {

            if(mMap !=null) {
                showLocationOnmap();
            }
        }

        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)

                current_latitude = location.getLatitude();
            assert location != null;
            current_longitude = location.getLongitude();


            if(is_check_pickup_or_drop==0) {
                    if(pickup_location_address.isEmpty()) {

                        pickup_latitude = location.getLatitude();
                        pickup_longitude = location.getLongitude();
                        clearMap();
                        latLng = new LatLng(pickup_latitude, pickup_longitude);
                        A_address = getAddress(latLng);
                        edt_pickup_location.setText(A_address);
                        pickup_location_address = A_address;
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_location))
                                .anchor(0.5f, 0.5f)
                                .title(A_address);
                        mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    }
                }
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.mMap = googleMap;
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
            this.mMap.setMapStyle(style);


            if (is_check_pickup_or_drop == 1) {
                showLocationOnmap();
            }
            if (is_check_pickup_or_drop == 2) {
                showLocationOnmap();
            }
            if (is_check_pickup_or_drop != 0) {
                showLocationOnmap();
            }

           /* if(pickup_location_address.equals(""))
              // getMyLocation();
            }*/
        }
    }


    /*Poly Line Googe Map...*/

    public void showLocationOnmap() {

        GloableVariable.Tag_pickup_contact_name = User.getInstance().getFirstName() ;
        GloableVariable.Tag_pickup_contact_number = User.getInstance().getMobileNumber();

        GloableVariable.Tag_drop_contact_name = User.getInstance().getFirstName() ;
        GloableVariable.Tag_drop_contact_number = User.getInstance().getMobileNumber();

        clearMap();
        markerList = new ArrayList<>();
        addMarkere(pickup_latitude, pickup_longitude, "", R.drawable.pin_location_pin);
        addMarkere(drop_latitude,drop_longitude, "", R.drawable.drop_location_pin);
        prepareRouteUrl(pickup_latitude,pickup_longitude ,drop_latitude,drop_longitude);
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

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(v.getPosition().latitude, v.getPosition().longitude));
        builder.include(new LatLng(parseDouble.getPosition().latitude, parseDouble.getPosition().longitude));
        LatLngBounds bounds = builder.build();
        mMap.setPadding(200, 650, 200, 590);
        CameraPosition cameraPosition = new CameraPosition.Builder()

                .target(new LatLng(v.getPosition().latitude, v.getPosition().longitude))
                .zoom(11.10f)
                .tilt(0)
                .bearing(300)
                .build();

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