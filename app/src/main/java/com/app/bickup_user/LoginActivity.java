package com.app.bickup_user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.controller.NetworkCallBack;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.fragments.LoginFragment;
import com.app.bickup_user.fragments.Signupfragment;
import com.app.bickup_user.interfaces.HandleLoginSignUpNavigation;
import com.app.bickup_user.model.Socialuser;
import com.app.bickup_user.model.User;
import com.app.bickup_user.push_notification.SharedPrefManager;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements InternetConnectionBroadcast.ConnectivityRecieverListener,HandleLoginSignUpNavigation,NetworkCallBack, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private CoordinatorLayout mCoordinatorLayout;
    private boolean mIsConnected;
    public static String TAG=LoginActivity.class.getSimpleName();
    private Activity mActivityreference;
    private Snackbar snackbar;
    private GoogleApiClient mGoogleApiClient;
    private CircularProgressView circularProgressBar;
    public static final int REQUEST_CREATEUSER=101;
    public static final int REQUEST_SOCIAL_LOGIN=102;
    public static final int REQUEST_SOCIAL_SIGNUP=104;
    public static final int REQUEST_LOGIN=103;
    public static  boolean REQUEST_NOT_VALID=false;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    boolean isfacebookSignUp=false;
    private String message;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = SharedPrefManager.getInstance(this).getDeviceToken();
        if (token != null) {
            GloableVariable.Tag_Is_device_token=token;
            Log.d("Tag_Is_device_token : ",GloableVariable.Tag_Is_device_token);
        } else {
            // GloableVariable.Tag_Is_device_token="fWQKCxXenHA:APA91bFJT1YJZgEaME5Co75b_cR1scI9NtrcjFzja8pF-oc0Fiw5xcXGIO-O2-Ak7lwYJAi6i8yEiSVjFRQFa_HH7w58Gxlp4132r3jl3XexxAbgYu93Fs0D3h7sVUvz_bfbv61HWAIs";

        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_login);
        mActivityreference=this;

/*

        SharedPreferences sharedPreferences=this.getSharedPreferences("onTokenRefresh",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",GloableVariable.Tag_Is_device_token);
        editor.commit();


         sharedPreferences=this.getSharedPreferences("onTokenRefresh",MODE_PRIVATE);
         GloableVariable.Tag_Is_device_token=sharedPreferences.getString("token","");
         Log.d("onTokenRefresh",GloableVariable.Tag_Is_device_token);
*/

        googleSignIn();
        faceBookinSignIn();
        initializeViews();
        callLoginfragment();
    }

    private void faceBookinSignIn() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Bundle bFacebookData = getFacebookData(object);
                                LoginManager.getInstance().logOut();
                                handlefacebookdata(bFacebookData);
                            }


                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    private void handlefacebookdata(Bundle bFacebookData) {
        String firstname=bFacebookData.getString("first_name");
        String lastName=bFacebookData.getString("last_name");
        String email=bFacebookData.getString("email");
        String facebookID=bFacebookData.getString("idFacebook");
        String profilepic=bFacebookData.getString("profile_pic");
        String socialType="facebook";

        Socialuser.getInstance().setFirstName(firstname);
        Socialuser.getInstance().setLastName(lastName);
        Socialuser.getInstance().setEmail(email);
        Socialuser.getInstance().setImage(profilepic);
        Socialuser.getInstance().setSocialtype(socialType);
        Socialuser.getInstance().setFacebooID(facebookID);
        if(isfacebookSignUp){
         callSignupFragmentForSocialLogin();
        }else {
            callAPIForGoogleSignIn();
        }
    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("" +
                        "+", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email")) {
                String email = object.getString("email");
                bundle.putString("email", email);
            }
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d(TAG,"Error parsing JSON");
        }
        return null;
    }




    private void callLoginfragment() {
        LoginFragment loginFragment=new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_activity_container,loginFragment).commit();
    }

    private void googleSignIn() {
        GoogleSignInOptions mGoogleSignInOption= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

         mGoogleApiClient=new GoogleApiClient.Builder(this)
                 .enableAutoManage(this,this)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener(this)
                 .addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOption)
                 .build();
    }


    private void signInWithGoogle(){
        Intent mIntent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mIntent, ConstantValues.GOOGLE_SIGN_IN);
    }

    private void signUPWithGoogle(){
        Intent mIntent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mIntent, ConstantValues.GOOGLE_SIGN_UP);
    }


    private void initializeViews() {
        mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.cordinatorlayout);
        circularProgressBar=(CircularProgressView)findViewById(R.id.progress_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantValues.GOOGLE_SIGN_IN||requestCode==ConstantValues.GOOGLE_SIGN_UP) {

            GoogleSignInResult mGoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(mGoogleSignInResult!=null) {
                int code = mGoogleSignInResult.getStatus().getStatusCode();
                signOut();
                handleGoogleSignInResult(mGoogleSignInResult, requestCode);
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult mGoogleSignInResult, int requestCode) {
        String firstName="",lastname="",socialType="google";
        if(mGoogleSignInResult.isSuccess()){
            GoogleSignInAccount signInAccount=mGoogleSignInResult.getSignInAccount();
            String googleID=signInAccount.getId();
            String email=signInAccount.getEmail();
            String name=signInAccount.getDisplayName();
            Uri image=signInAccount.getPhotoUrl();
            String imageString=String.valueOf(image);
            if(name.contains(" ")){
                String[] array=name.split(" ");
                firstName=array[0];
                lastname=array[1];
            }
            Socialuser.getInstance().setFirstName(firstName);
            Socialuser.getInstance().setLastName(lastname);
            Socialuser.getInstance().setEmail(email);
            Socialuser.getInstance().setImage(imageString);
            Socialuser.getInstance().setSocialtype(socialType);
            Socialuser.getInstance().setGoogleID(googleID);
            if(requestCode==ConstantValues.GOOGLE_SIGN_UP){
                callSignupFragmentForSocialLogin();
            }else {
                callAPIForGoogleSignIn();
            }

        }
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    private void callAPIForGoogleSignIn() {
        String createUserUrl= WebAPIManager.getInstance().getSocialLoginUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.GOOGLE_ID, Socialuser.getInstance().getGoogleID());
        requestBody.addProperty(ConstantValues.FACEBOOK_ID, Socialuser.getInstance().getFacebooID());
        requestBody.addProperty(ConstantValues.Is_Device_Key, ConstantValues.Is_device_token);
        requestBody.addProperty(ConstantValues.SOCIAL_TYPE, Socialuser.getInstance().getSocialtype());
        userLogin(requestBody,createUserUrl,this,60*1000,REQUEST_SOCIAL_LOGIN);

    }

    public void  prepareUserForSocialSignUP() {
        String createUserUrl= WebAPIManager.getInstance().getSocialSignupUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.USER_FIRSTNAME, Socialuser.getInstance().getFirstName());
        requestBody.addProperty(ConstantValues.USER_LASTNAME,Socialuser.getInstance().getLastName());
        requestBody.addProperty(ConstantValues.USER_EMAILADDRESS, Socialuser.getInstance().getEmail());
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER, Socialuser.getInstance().getMobileNumber());
        requestBody.addProperty(ConstantValues.FACEBOOK_ID, Socialuser.getInstance().getFacebooID());
        requestBody.addProperty(ConstantValues.GOOGLE_ID, Socialuser.getInstance().getGoogleID());
        requestBody.addProperty(ConstantValues.SOCIAL_TYPE, Socialuser.getInstance().getSocialtype());
        requestBody.addProperty(ConstantValues.COUNTRY_CODE, Socialuser.getInstance().getCountryCode());
        requestBody.addProperty(ConstantValues.Is_Device_Key, ConstantValues.Is_device_token);
        requestBody.addProperty("profile_image", Socialuser.getInstance().getImage());
        userLogin(requestBody,createUserUrl,this,60*1000,REQUEST_SOCIAL_SIGNUP);
    }


    public void  prepareUnVerifiedUser(final String firstname, final String lastname, final String mobileNumber, final String email, final String password,String countryCode) {
       String createUserUrl= WebAPIManager.getInstance().getCreateUserUrl();
       final JsonObject requestBody=new JsonObject();
           requestBody.addProperty(ConstantValues.USER_FIRSTNAME, firstname);
           requestBody.addProperty(ConstantValues.USER_LASTNAME,lastname);
           requestBody.addProperty(ConstantValues.USER_EMAILADDRESS, email);
           requestBody.addProperty(ConstantValues.USER_MOBILENUMBER, mobileNumber);
           requestBody.addProperty(ConstantValues.USER_PASSWORD, password);
           requestBody.addProperty(ConstantValues.COUNTRY_CODE, countryCode);
           requestBody.addProperty(ConstantValues.COUNTRY_CODE, countryCode);
           requestBody.addProperty(ConstantValues.Is_Device_Key, GloableVariable.Tag_Is_device_token);

        createUnVerifieduser(requestBody,createUserUrl,this,60*1000,101);
   }

    private void createUnVerifieduser(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(createUserUrl)
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if(e!=null){
                            circularProgressBar.setVisibility(View.GONE);
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
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 201:
                                loginActivity.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void  prepareuserLogin(final String mobileNumber, final String password,final String countryCode) {

         String createUserUrl= WebAPIManager.getInstance().getUserLoginUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER, mobileNumber);
        requestBody.addProperty(ConstantValues.USER_PASSWORD, password);
        requestBody.addProperty(ConstantValues.COUNTRY_CODE, countryCode);
        requestBody.addProperty(ConstantValues.Is_Device_Key, GloableVariable.Tag_Is_device_token);
        userLogin(requestBody,createUserUrl,this,60*1000,REQUEST_LOGIN);
    }


    private void userLogin(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("POST",createUserUrl)
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
    protected void onStop() {
        super.onStop();
        isfacebookSignUp=false;
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode){
            case REQUEST_CREATEUSER:
                ParseCreateUserresponse parseCreateUserresponse=new ParseCreateUserresponse();
                parseCreateUserresponse.execute(data.toString());
                break;
            case REQUEST_LOGIN:
                ParseuserLoginResponse parseuserLoginResponse=new ParseuserLoginResponse();
                parseuserLoginResponse.execute(data.toString());
                break;
            case REQUEST_SOCIAL_LOGIN:
                ParseuserSocialLoginResponse parseuserSocialLoginResponse=new ParseuserSocialLoginResponse();
                parseuserSocialLoginResponse.execute(data.toString());
                break;
            case REQUEST_SOCIAL_SIGNUP:
                ParseCreateUserresponse socialSignup=new ParseCreateUserresponse();
                socialSignup.execute(data.toString());
                break;
        }
    }



    @Override
    public void onError(String msg){
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    class ParseuserLoginResponse extends AsyncTask<String,Void,HashMap<String,String>>{

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email,accessToken,phoneNumber,userId,message,flag="0";
            HashMap<String,String> map=new HashMap<>();
            String response=strings[0];
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject data=jsonObject.getJSONObject("response");
                message= jsonObject.getString("message");
                flag= jsonObject.getString("flag");
                map.put("flag",flag);
                map.put("message",message);
                map.put(ConstantValues.USER_EMAILADDRESS,data.getString("email"));
                map.put(ConstantValues.USER_MOBILENUMBER,data.getString("phone_number"));
                map.put(ConstantValues.USER_ID,data.getString("user_id"));
                map.put(ConstantValues.USER_ACCESS_TOKEN,data.getString("access_token"));
                map.put(ConstantValues.USER_FIRSTNAME,data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME,data.getString("last_name"));
                map.put(ConstantValues.COUNTRY_CODE,data.getString("country_code"));
                JSONObject jsonArray=data.getJSONObject("profile_image");
                map.put(ConstantValues.USER_IMAGE,jsonArray.getString("image_url"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
             String flag=hashMap.get("flag");
            String message=hashMap.get("message");
            String userimage=hashMap.get(ConstantValues.USER_IMAGE);
            if(userimage!=null) {
                if (userimage.contains("http")) {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), hashMap.get(ConstantValues.USER_IMAGE));
                } else {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE));
                }
            }else{
                User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE),"");
            }

            if(flag.equalsIgnoreCase("3")){
                callForgotAndReset(ConstantValues.OTP,1);
            }else{
                User.getInstance().setVarified(true, LoginActivity.this,true);
                callmainActivity();
            }
            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }

    class ParseCreateUserresponse extends AsyncTask<String,Void,HashMap<String,String>>{

    @Override
    protected HashMap<String, String> doInBackground(String... strings) {
        String email,accessToken,phoneNumber,userId,message,flag="0";
        HashMap<String,String> map=new HashMap<>();
        String response=strings[0];
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject data=jsonObject.getJSONObject("response");
            message= jsonObject.getString("message");
            flag= jsonObject.getString("flag");
            map.put("flag",flag);
            map.put("message",message);
            map.put(ConstantValues.USER_EMAILADDRESS,data.getString("email"));
            map.put(ConstantValues.USER_MOBILENUMBER,data.getString("phone_number"));
            map.put(ConstantValues.USER_ID,data.getString("user_id"));
            map.put(ConstantValues.USER_ACCESS_TOKEN,data.getString("access_token"));
            map.put(ConstantValues.USER_FIRSTNAME,data.getString("first_name"));
            map.put(ConstantValues.USER_LASTNAME,data.getString("last_name"));
            map.put(ConstantValues.COUNTRY_CODE,data.getString("country_code"));
            JSONObject jsonArray=data.getJSONObject("profile_image");
            map.put(ConstantValues.USER_IMAGE,jsonArray.getString("image_url"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");
            if(flag.equalsIgnoreCase("2")) {
                String userImage = hashMap.get(ConstantValues.USER_IMAGE);
                if (userImage != null){
                if (userImage.contains("http")) {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), hashMap.get(ConstantValues.USER_IMAGE));
                } else {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE));
                }
                    callForgotAndReset(ConstantValues.OTP, 1);
            }else {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), "");
                    callForgotAndReset(ConstantValues.OTP, 1);
                }
            }
            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }




    class ParseuserSocialLoginResponse extends AsyncTask<String,Void,HashMap<String,String>>{

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email,accessToken,phoneNumber,userId,message,flag="0";
            HashMap<String,String> map=new HashMap<>();
            String response=strings[0];
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject data=jsonObject.getJSONObject("response");
                message= jsonObject.getString("message");
                flag= jsonObject.getString("flag");
                map.put("flag",flag);
                map.put("message",message);
                if(flag.equalsIgnoreCase("1")||flag.equalsIgnoreCase("2")){
                    map.put(ConstantValues.USER_EMAILADDRESS,data.getString("email"));
                    map.put(ConstantValues.USER_MOBILENUMBER,data.getString("phone_number"));
                    map.put(ConstantValues.USER_ID,data.getString("user_id"));
                    map.put(ConstantValues.USER_ACCESS_TOKEN,data.getString("access_token"));
                    map.put(ConstantValues.USER_FIRSTNAME,data.getString("first_name"));
                    map.put(ConstantValues.USER_LASTNAME,data.getString("last_name"));
                    map.put(ConstantValues.COUNTRY_CODE,data.getString("country_code"));
                    JSONObject jsonArray=data.getJSONObject("profile_image");
                    map.put(ConstantValues.USER_IMAGE,jsonArray.getString("image_url"));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");
            if(flag.equalsIgnoreCase("1")||flag.equalsIgnoreCase("2")){
                String userImage=hashMap.get(ConstantValues.USER_IMAGE);
                if(userImage!=null) {
                    if (userImage.contains("http")) {
                        User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), hashMap.get(ConstantValues.USER_IMAGE));
                    } else {
                        User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE));
                    }
                }else {
                    User.getInstance().createUser(LoginActivity.this, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, true, hashMap.get(ConstantValues.COUNTRY_CODE), "");
                }

                if(flag.equalsIgnoreCase("1")){
                    User.getInstance().setVarified(true, LoginActivity.this,true);
                    callmainActivity();
                }else {
                    callForgotAndReset(ConstantValues.OTP,1);
                }


            }else{
                if(flag.equalsIgnoreCase("3")) {
                    callSignupFragmentForSocialLogin();
                }
            }
            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    protected void onResume() {
        super.onResume();
        isfacebookSignUp=false;
        checkInternetconnection();
        if (AppController.getInstance()!=null) {
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
        mIsConnected=isConnected;
        if(isConnected){
            if(snackbar!=null) {
                snackbar.dismiss();
            }
        }else{
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    public void showSnackBar(String mString){
       /* snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
    }

    @Override
    public void performSignIn(String mobileNumber, String password, String countryCode) {
        prepareuserLogin(mobileNumber,password,countryCode);
    }

    @Override
    public void performGoogleSignIn(boolean mIsConnected) {
        if(mIsConnected){
            signInWithGoogle();
        }else {
            signUPWithGoogle();
        }

    }

    @Override
    public void performFacebookSignIn(boolean isSignup) {
        if(mIsConnected){
        }else {
            isfacebookSignUp=true;
        }
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    @Override
    public void performSocialSignUp() {
        prepareUserForSocialSignUP();
    }

    @Override
    public void performSignUp(String firstname, String lastname, String mobileNumber, String email, String password,String countryCode)  {
        prepareUnVerifiedUser(firstname,lastname,mobileNumber,email,password,countryCode);
    }

    private void callmainActivity() {
        Intent intent=new Intent(mActivityreference,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void callSignupFragment() {
        Signupfragment mSignupFargment=new Signupfragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_activity_container,mSignupFargment).commit();
    }


    public void callSignupFragmentForSocialLogin() {
        Fragment mSignupFargment=new Signupfragment();
        Bundle bundle=new Bundle();
        bundle.putInt("Social",REQUEST_SOCIAL_LOGIN);
        mSignupFargment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.login_activity_container,mSignupFargment).commit();
    }

    @Override
    public void callSigninFragment() {
        callLoginfragment();
    }

    @Override
    public void callForgotAndReset(int checkForgotReset,int changeNumber) {
        Intent intent=new Intent(this,ResetAndForgetPasswordActivity.class);
        intent.putExtra(ConstantValues.CHOOSE_PAGE,checkForgotReset);
        intent.putExtra(ConstantValues.CHANGE_NUMBER,changeNumber);
        startActivity(intent);
    }

    @Override
    public void callMainActivity() {
        callmainActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage(getResources().getString(R.string.txt_close_app))
                .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.txt_No), null)
                .show();
    }
}

