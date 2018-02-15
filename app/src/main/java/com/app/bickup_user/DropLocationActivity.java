package com.app.bickup_user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.app.bickup_user.GlobleVariable.GloableVariable.Tag_drop_location_address;
import static com.app.bickup_user.GlobleVariable.GloableVariable.is_check_pickup_or_drop;

public class DropLocationActivity extends AppCompatActivity implements View.OnClickListener,
        NetworkCallBack ,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ImageView imgBack;
    private EditText edtPickupLocation;
    private EditText edtFloorNumber;
    private EditText edtUnitNumber;
    private EditText edtContactPersonname;
    private EditText edtContactPersonNumber;
    private EditText edtComments;
    private LinearLayout liBuilding;
    private LinearLayout liVilla;
    private TextView txtMe;
    private TextView txtOther;
    private ImageView imgBuilding;
    private ImageView imgVilla;

    private TextView txtBuildings;
    private TextView txtVilla;
    private LinearLayout liMe;
    private LinearLayout liOther;
    private TextView imgSearch;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private double lattitude;
    private double longitude;
    private String address;
    private EditText edtBuildingName;
    private LinearLayout liBuildingDetails;
    private TextView txtBuildingName;
    private CircularProgressView circularProgressBar;
    private String message;




    /*GoogleMap Pin Moveable...*/

    //-----------------------------------------Map-------------
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LatLng mCenterLatLong;
    private ImageView imageMarker, btn_current_location,imageView123;
    private double current_latitude = 0.0;
    private double current_longitude = 0.0;
    public static String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private String current_adress;
    private ScrollView scrollview;

    private SharedPreferences pref_drop;
    private double drop_latitude,drop_longitude;
    private String drop_location_address;


    private int isChekDrop=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        is_check_pickup_or_drop=2;

        setContentView(R.layout.activity_pickup_location);

        pref_drop = getSharedPreferences("MyDrop", Context.MODE_PRIVATE);


        initializeViews();
        initMap();
        GloableVariable.Tag_check_locaton_type=2;
        GloableVariable.Tag_drop_home_type="2";
        GloableVariable.Tag_drop_Contact_type="1";
        CommonMethods.getInstance().hideSoftKeyBoard(this);


    }

    private void initializeViews() {
        circularProgressBar = (CircularProgressView) findViewById(R.id.progress_view);
        TextView txtPickup = (TextView) findViewById(R.id.txt_pickup);
        TextView labelFeilddetails = (TextView) findViewById(R.id.txt_label_filldetails);
        labelFeilddetails.setText(getResources().getString(R.string.txt_please_fill_drop_location_details));
        RelativeLayout search_container=(RelativeLayout)findViewById(R.id.search_container);

        TextView labelDropContacts = (TextView) findViewById(R.id.txt_label_fill_contact_details);
        labelDropContacts.setText(getResources().getString(R.string.txt_please_fill_drop_contact_details));
        txtPickup.setText(getResources().getString(R.string.txt_drop));
        TextView txtHeader = (TextView) findViewById(R.id.txt_activty_header);
        findViewById(R.id.btn_confirm_booking).setOnClickListener(this);
        txtHeader.setText(getResources().getString(R.string.txt_drop));
        imgBack = (ImageView) findViewById(R.id.backImage_header);
        ImageView imageView = (ImageView) findViewById(R.id.img_pickup);
        imageView.setImageResource(R.drawable.drop_location);

        edtPickupLocation = (EditText) findViewById(R.id.edt_pickupLocation);
        edtFloorNumber = (EditText) findViewById(R.id.edt_floor_number);
        edtUnitNumber = (EditText) findViewById(R.id.edt_unit_number);
        edtContactPersonname = (EditText) findViewById(R.id.edt_contact_peron_name);
        edtContactPersonNumber = (EditText) findViewById(R.id.edt_edt_contact_person_number);
        edtComments = (EditText) findViewById(R.id.edt_comments);
        txtMe = (TextView) findViewById(R.id.txt_me);
        txtOther = (TextView) findViewById(R.id.txt_other);
        txtBuildings = (TextView) findViewById(R.id.txt_building);
        txtVilla = (TextView) findViewById(R.id.txt_villa);

        liBuildingDetails = (LinearLayout) findViewById(R.id.li_building_details);
        txtBuildingName = (TextView) findViewById(R.id.txt_building_name);


        edtBuildingName = (EditText) findViewById(R.id.edt_building_name);

        imgBuilding = (ImageView) findViewById(R.id.img_building);
        imgVilla = (ImageView) findViewById(R.id.img_villa);
        imgSearch = (TextView) findViewById(R.id.txt_pickup);
        imgSearch.setOnClickListener(this);


        liBuilding = (LinearLayout) findViewById(R.id.li_building);
        liVilla = (LinearLayout) findViewById(R.id.li_villa);
        liBuilding.setTag(true);
        liVilla.setTag(true);

        liMe = (LinearLayout) findViewById(R.id.li_me);
        liOther = (LinearLayout) findViewById(R.id.li_other);
        liBuildingDetails.setTag(true);
        liOther.setTag(false);
        liMe.setTag(true);

        liMe.setOnClickListener(this);
        liOther.setOnClickListener(this);
        liBuilding.setOnClickListener(this);
        liVilla.setOnClickListener(this);

        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        edtContactPersonname.setText(User.getInstance().getFirstName()  +" "+User.getInstance().getLastName());
        edtContactPersonNumber.setText(User.getInstance().getMobileNumber());

        GloableVariable.Tag_drop_contact_name=User.getInstance().getFirstName()+" "+User.getInstance().getLastName();
        GloableVariable.Tag_drop_contact_number=User.getInstance().getMobileNumber();


        //----------------------------
        edtPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonMethods.getInstance().hideSoftKeyBoard(DropLocationActivity.this);
                openAutoComplePicker();
            }
        });

        search_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonMethods.getInstance().hideSoftKeyBoard(DropLocationActivity.this);
                openAutoComplePicker();
            }
        });







        //  address=GloableVariable.Tag_drop_location_address;
        edtPickupLocation.setText(address);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                isChekDrop=1;
                Place place = PlaceAutocomplete.getPlace(this, data);
                address= (String) place.getAddress();
                if(address.contains(",")) {
                    String[] array = address.split(",", 3);
                    try {
                        address = array[0] + "," + array[1] + "," + array[2];
                    }catch (Exception e){}
                }
                address = (String) place.getAddress();

                edtPickupLocation.setText(address);
                LatLng latLng= place.getLatLng();
                drop_location_address=address;
                drop_latitude=latLng.latitude;
                drop_longitude=latLng.longitude;
                clearMap();
                imageMarker.setImageResource(R.drawable.ic_pin_drop);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            } else if (resultCode == RESULT_CANCELED) {}
        }
    }

    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                finish();
                break;
            case R.id.li_me:
                liMe.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liOther.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtMe.setTextColor(this.getResources().getColor(R.color.white));
                txtOther.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                liMe.setTag(true);
                liOther.setTag(false);
                edtContactPersonname.setText(User.getInstance().getFirstName()  +" "+User.getInstance().getLastName() );
                edtContactPersonNumber.setText(User.getInstance().getMobileNumber());

                GloableVariable.Tag_drop_Contact_type="1";

                break;
            case R.id.li_other:
                liOther.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liMe.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtOther.setTextColor(this.getResources().getColor(R.color.white));
                txtMe.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                liOther.setTag(true);
                liMe.setTag(false);
                edtContactPersonname.setText("");
                edtContactPersonNumber.setText("");
                GloableVariable.Tag_drop_Contact_type="2";
                break;

            case R.id.li_building:
                liBuilding.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liVilla.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtBuildings.setTextColor(this.getResources().getColor(R.color.white));
                txtVilla.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                imgBuilding.setImageResource(R.drawable.ac_home);
                imgVilla.setImageResource(R.drawable.de_villa);
                liBuildingDetails.setVisibility(View.VISIBLE);
                txtBuildingName.setText(getResources().getString(R.string.txt_building_name));
                liBuilding.setTag(true);
                liVilla.setTag(false);
                GloableVariable.Tag_drop_home_type="2";
                break;
            case R.id.li_villa:
                liBuilding.setBackgroundColor(this.getResources().getColor(R.color.white));
                liVilla.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                txtBuildings.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                txtVilla.setTextColor(this.getResources().getColor(R.color.white));
                imgBuilding.setImageResource(R.drawable.de_home);
                imgVilla.setImageResource(R.drawable.ac_villa);
                txtBuildingName.setText(getResources().getText(R.string.txt_villa_number));
                liBuildingDetails.setVisibility(View.GONE);
                liBuilding.setTag(false);
                liVilla.setTag(true);
                GloableVariable.Tag_drop_home_type="3";
                break;
            case R.id.txt_pickup:
                CommonMethods.getInstance().hideSoftKeyBoard(this);
                openAutoComplePicker();
                break;
            case R.id.btn_confirm_booking:
                if (validateFields()) {
                    String contactPersonName, contactPersonNumber, buildingName = "", florNumber = "", comment = "", unitNumber = "", locationType = "1";
                    if ((boolean) liBuilding.getTag()) {
                        florNumber = edtFloorNumber.getText().toString().trim();
                        unitNumber = edtUnitNumber.getText().toString().trim();
                        locationType = "2";


                    }
                    if ((boolean) liMe.getTag()) {
                        contactPersonName = User.getInstance().getFirstName() + " " + User.getInstance().getLastName();
                        contactPersonNumber = User.getInstance().getMobileNumber();

                        GloableVariable.Tag_drop_contact_name=contactPersonName;
                        GloableVariable.Tag_drop_contact_number=contactPersonNumber;

                    } else {
                        contactPersonName = edtContactPersonname.getText().toString().trim()  +" "+User.getInstance().getLastName();
                        contactPersonNumber = edtContactPersonNumber.getText().toString().trim();

                        GloableVariable.Tag_drop_contact_name=contactPersonName;
                        GloableVariable.Tag_drop_contact_number=contactPersonNumber;

                    }
                    comment = edtComments.getText().toString().trim();
                    buildingName = edtBuildingName.getText().toString().trim();
                    String isMe = "2";
                    if ((boolean) liMe.getTag()) {
                        isMe = "1";
                    }
                    prepareBooking(String.valueOf(lattitude), String.valueOf(longitude), buildingName, buildingName, florNumber, unitNumber, contactPersonName, contactPersonNumber, comment, isMe, locationType);



                    if (GloableVariable.Tag_drop_home_type.equals("2")) {
                        GloableVariable.Tag_drop_villa_no = "";
                        GloableVariable.Tag_drop_building_name = buildingName;
                    }
                    if (GloableVariable.Tag_drop_home_type.equals("3")) {
                        GloableVariable.Tag_drop_villa_no = buildingName;
                        GloableVariable.Tag_drop_building_name = "";
                    }


                    if (GloableVariable.Tag_drop_Contact_type.equals("1")) {
                        GloableVariable.Tag_drop_villa_no = "";
                        GloableVariable.Tag_drop_building_name = buildingName;
                    }
                    if (GloableVariable.Tag_drop_Contact_type.equals("2")) {
                        GloableVariable.Tag_drop_villa_no = buildingName;
                        GloableVariable.Tag_drop_building_name = "";
                    }


                    GloableVariable.Tag_drop_floor_number = florNumber;
                    GloableVariable.Tag_drop_unit_number = unitNumber;
                    GloableVariable.Tag_drop_location_check ="3";



                    //Drop Location save..............
                    SharedPreferences.Editor p_edit = pref_drop.edit();
                    p_edit.putString("key_drop_lat", "" +drop_latitude);
                    p_edit.putString("key_drop_long", "" + drop_longitude);
                    p_edit.putString("key_drop_address",drop_location_address);
                    p_edit.apply();
                    finishActivit();
                }
                GloableVariable.Tag_drop_comments=edtComments.getText().toString().trim();

                break;

        }

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private boolean validateFields() {

        if (!CommonMethods.getInstance().validateEditFeild(edtPickupLocation.getText().toString().trim())) {
            Toast.makeText(this, this.getResources().getString(R.string.txt_vaidate_drop_location), Toast.LENGTH_SHORT).show();
            return false;
        }
      /*  if (!CommonMethods.getInstance().validateEditFeild(edtBuildingName.getText().toString())) {
            if((boolean)liBuilding.getTag()) {
                Toast.makeText(this, this.getResources().getString(R.string.txt_vaidate_building_name), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, this.getResources().getString(R.string.txt_vaidate_villa_no), Toast.LENGTH_SHORT).show();
            }

            return false;
        }*/
        if((boolean)liOther.getTag()){
            if (!CommonMethods.getInstance().validateEditFeild(edtContactPersonname.getText().toString())) {
                Toast.makeText(this, this.getResources().getString(R.string.txt_vaidate_contact_person_name), Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!CommonMethods.getInstance().validateMobileNumber(edtContactPersonNumber.getText().toString().trim(),6)) {
                Toast.makeText(this, this.getResources().getString(R.string.txt_vaidate_contact_persson_number), Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        return true;
    }
    private void finishActivit() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ConstantValues.LATITUDE, lattitude);
        returnIntent.putExtra(ConstantValues.LONGITUDE, longitude);
        returnIntent.putExtra(ConstantValues.DROP_ADDRESS, address);
        saveLocationToPreferenxes(returnIntent);
    }

    private void saveLocationToPreferenxes(Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues.USER_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(ConstantValues.DROP_LATITUDE, String.valueOf(lattitude));
        edit.putString(ConstantValues.DROP_LONGITUDE, String.valueOf(longitude));
        edit.putString(ConstantValues.DROP_ADDRESS, address);
        boolean isSaved = edit.commit();
        if (isSaved) {
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void openAutoComplePicker() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .setCountry("AE")
                    // .setCountry("IN")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            // .setBoundsBias(new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)))
                            .setBoundsBias(new LatLngBounds(new LatLng(25.276987, 55.296249), new LatLng(28.20453, 97.34466)))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    public void  prepareBooking(final String lattitude, final String longitude,final String buildinName,final String villaNumber,String floorNumber,String unitNumber,String contactPersonName,String contactPersonNumber,String comments,String contact,String locationType) {
        String createUserUrl= WebAPIManager.getInstance().getSaveAddressUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.LATITUDE, lattitude);
        requestBody.addProperty(ConstantValues.LONGITUDE, longitude);
        requestBody.addProperty(ConstantValues.BUILDING_NAME, buildinName);
        requestBody.addProperty(ConstantValues.VILLA_NAME, villaNumber);
        requestBody.addProperty(ConstantValues.FLOOR_NUMBER, floorNumber);
        requestBody.addProperty(ConstantValues.UNIT_NUMBER, unitNumber);
        requestBody.addProperty(ConstantValues.ADDRESS, address);
        requestBody.addProperty(ConstantValues.CONTACT_PERSON_NAME, contactPersonName);
        requestBody.addProperty(ConstantValues.CONTACT_PERSON_NUMBER, contactPersonNumber);
        requestBody.addProperty(ConstantValues.COMMENTS, comments);
        requestBody.addProperty(ConstantValues.LOCATION_TYPE, locationType);
        requestBody.addProperty(ConstantValues.LOCATION_CONTACT, contact);
        callAPI(requestBody,createUserUrl,this,60*1000, LoginActivity.REQUEST_LOGIN);
    }


    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("POST",createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN,User.getInstance().getAccesstoken())
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if(e!=null){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value=String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            message = jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status){
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
                                loginActivity.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        ParseSaveBooking parseSaveBooking=new ParseSaveBooking();
        parseSaveBooking.execute(String.valueOf(data));
    }

    @Override
    public void onError(String msg) {

    }

    class ParseSaveBooking extends AsyncTask<String,Void,HashMap<String,String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject data = jsonObject.getJSONObject("response");
                map.put(ConstantValues.LATITUDE, data.getString("latitude"));
                map.put(ConstantValues.LONGITUDE, data.getString("longitude_pinmove"));
                map.put(ConstantValues.ADDRESS, data.getString("address"));
                map.put(ConstantValues.CONTACT_PERSON_NAME, data.getString("location_contact_name"));
                map.put(ConstantValues.CONTACT_PERSON_NUMBER, data.getString("location_contact_number"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String flag = hashMap.get("flag");
            String message = hashMap.get("message");
            finishActivit();

        }
    }



    /*Google map working.......*/

    private void initMap(){

        imageMarker = findViewById(R.id.imageMarker);
        imageMarker.setImageResource(R.drawable.ic_pin_drop);
        btn_current_location = findViewById(R.id.btn_current_location);
        scrollview = findViewById(R.id.scrollview);
        imageView123 = findViewById(R.id.imageView123);


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                try {
                    //  askLocationSettings();
                } catch (Exception e) {
                }

                try {
                    checkLocationPermission();
                } catch (Exception e) {
                }

                try {
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
        }


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
                            try {
                                //askLocationSettings();
                            } catch (Exception e) {
                            }

                            try {
                                checkLocationPermission();
                            } catch (Exception e) {
                            }


                            try {
                                getMyLocation();
                            } catch (Exception e) {
                            }

                            try {
                                buildGoogleApiClient();
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();

        if(isChekDrop==0) {
            drop_latitude = Double.parseDouble(pref_drop.getString("key_drop_lat", "1.2"));
            drop_longitude = Double.parseDouble(pref_drop.getString("key_drop_long", "1.2"));
            drop_location_address = pref_drop.getString("key_drop_address", "");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        this.mMap.setMapStyle(style);
        mMap.setPadding(0, 250, 0, 0);
        imageMarker.setImageResource(R.drawable.ic_pin_drop);


        if(!drop_location_address.isEmpty()) {
            edtPickupLocation.setText(Tag_drop_location_address);
            Marker();
        }else {
            clearMap();
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            current_adress = onCameraPositionChanged_Pickup(mMap.getCameraPosition());
                            drop_location_address = current_adress;
                            edtPickupLocation.setText(current_adress);

                        }
                    });

                }
            });
        }
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
        LatLng latLng = new LatLng(current_latitude, current_longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }


    private String onCameraPositionChanged_Pickup(CameraPosition position) {
        mCenterLatLong = position.target;
        mMap.clear();
        try {
            Location mLocation = new Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            drop_latitude=mCenterLatLong.latitude;;
            drop_longitude=mCenterLatLong.longitude;
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
            assert location != null;
            current_longitude = location.getLongitude();

            drop_latitude=location.getLatitude();
            drop_longitude=location.getLongitude();

            if(drop_location_address.isEmpty()) {
                Marker();
            }
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Marker(){
        clearMap();
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        current_adress = onCameraPositionChanged_Pickup(mMap.getCameraPosition());
                        drop_location_address = current_adress;
                        edtPickupLocation.setText(current_adress);
                    }
                });

            }
        });
        LatLng latLng = new LatLng(drop_latitude, drop_longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
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




