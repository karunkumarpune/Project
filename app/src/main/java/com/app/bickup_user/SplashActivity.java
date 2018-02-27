package com.app.bickup_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.model.User;
import com.app.bickup_user.push_notification.SharedPrefManager;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity implements InternetConnectionBroadcast.ConnectivityRecieverListener {
    private boolean mIsConnected;
    private Activity mActivityreference;
    private CoordinatorLayout mCoordinatorLayout;
    private Snackbar snackbar;

    private SharedPreferences pref_pickup;
    private SharedPreferences pref_drop;

    private SharedPreferences Image_Sp;
    private SharedPreferences Image_Sp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Image_Sp = this.getSharedPreferences("Image_Sp", 0);
        Image_Sp1 = this.getSharedPreferences("LoginInfos", 0);

        pref_pickup = getSharedPreferences("MyPickup", Context.MODE_PRIVATE);
        pref_drop = getSharedPreferences("MyDrop", Context.MODE_PRIVATE);
        SharedPreferences.Editor pic_edit = pref_pickup.edit();
        pic_edit.clear();
        pic_edit.apply();

        SharedPreferences.Editor drop_edit = pref_drop.edit();
        drop_edit.clear();
        drop_edit.apply();



        SharedPreferences.Editor editor=Image_Sp.edit();
        editor.clear();
        editor.apply();

        SharedPreferences.Editor editor1=Image_Sp1.edit();
        editor1.clear();
        editor1.apply();


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d("Refreshed token", "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivityreference=SplashActivity.this;
        initializeViews();
        checkUserIsLogin();

       // ConstantValues.Is_device_token = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    private void checkUserIsLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues.USER_PREFERENCES, Context.MODE_PRIVATE);
        String isLogin= sharedPreferences.getString(ConstantValues.ISLogin,"false");
        if(isLogin.equalsIgnoreCase("true")){
            String isVerified= sharedPreferences.getString(ConstantValues.ISverified,"false");
            String userFirstName=sharedPreferences.getString(ConstantValues.USER_FIRSTNAME,"");
            String lastname=sharedPreferences.getString(ConstantValues.USER_LASTNAME,"");
            String accessToken=sharedPreferences.getString(ConstantValues.USER_ACCESS_TOKEN,"");
            String mobileNumber=sharedPreferences.getString(ConstantValues.USER_MOBILENUMBER,"");
            String emailAddress=sharedPreferences.getString(ConstantValues.USER_EMAILADDRESS,"");
            String password=sharedPreferences.getString(ConstantValues.USER_PASSWORD,"");
            String countryCode=sharedPreferences.getString(ConstantValues.COUNTRY_CODE,"");
            String userID=sharedPreferences.getString(ConstantValues.USER_ID,"");
            String userImage=sharedPreferences.getString(ConstantValues.USER_IMAGE,"");
            if(isVerified.equalsIgnoreCase("true")){
                User.getInstance().createUser(SplashActivity.this,accessToken,emailAddress,userID,mobileNumber,userFirstName,lastname,password,true,false,countryCode,userImage);
                timerMethod(true);
            }
        }else {
            timerMethod(false);
        }
    }

    private void timerMethod(final boolean isLogin) {
        final Intent[] intent = new Intent[1];
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin){
                    intent[0] =new Intent(mActivityreference,MainActivity.class);
                }else {
                    intent[0] =new Intent(mActivityreference,LoginActivity.class);
                }

                //intent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               // intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
                startActivity(intent[0]);
                finish();

            }
        },2000);
    }

    private void initializeViews() {
        mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if(AppController.getInstance()!=null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    private void checkInternetconnection() {
        mIsConnected= CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if(mIsConnected){
            if(snackbar!=null) {
                snackbar.dismiss();

            }
        }else{
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if(snackbar!=null) {
                snackbar.dismiss();
            }
        }else{
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
    public void showSnackBar(String mString){
        snackbar = Snackbar.make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
    }
}
