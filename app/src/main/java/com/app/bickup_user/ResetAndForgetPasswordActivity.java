package com.app.bickup_user;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.controller.NetworkCallBack;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.fragments.ForgotPassword;
import com.app.bickup_user.fragments.Otp;
import com.app.bickup_user.fragments.ResetPassword;
import com.app.bickup_user.interfaces.HandleForgotResetNavigations;
import com.app.bickup_user.model.HandleForgotuser;
import com.app.bickup_user.model.User;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.app.bickup_user.R.string.otp;


public class ResetAndForgetPasswordActivity extends AppCompatActivity implements HandleForgotResetNavigations, InternetConnectionBroadcast.ConnectivityRecieverListener,View.OnClickListener,NetworkCallBack {


    private TextView tv_header;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;
    private ImageView imgBack;
    private int changeNumber=0;
    public static  boolean isForgot=false;
    public static boolean isChangeNumber=false;
    private CircularProgressView circularProgressBar;
    public  static final int REQUEST_VERIFY_USER=101;
    public  static final int REQUEST_RESEND_OTP=102;
    public  static final int REQUEST_FORGOT_PASSWORD=103;
    public  static final int REQUEST_RESET_PASSWORD=104;
    public  static final int REQUEST_CHANGENUMBER=105;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_reset_and_forget_password);
        initViews();
        getdata();
    }

    private void getdata() {
        int pageToOpen=getIntent().getIntExtra(ConstantValues.CHOOSE_PAGE,0);
        changeNumber=getIntent().getIntExtra(ConstantValues.CHANGE_NUMBER,0);
        switch (pageToOpen){
            case ConstantValues.FORGOT_PASSWORD:
                tv_header.setText(getString(R.string.txt_forgot_pasword));
                ForgotPassword forgotPassword=new ForgotPassword();
                callfragment( forgotPassword,ForgotPassword.TAG);
                break;
            case ConstantValues.RESET_PASSWORD:
                tv_header.setText(getString(R.string.txt_reset_pasword));
                ResetPassword resetPassword=new ResetPassword();
                callfragment( resetPassword,ResetPassword.TAG);
                tv_header.setText(getString(R.string.enter_otp));
                break;
            case ConstantValues.OTP:
                tv_header.setText(getString(otp));
                Fragment otp=new Otp();
                Bundle bundle=new Bundle();
                bundle.putInt(ConstantValues.CHANGE_NUMBER,changeNumber);
                otp.setArguments(bundle);
                callfragment( otp,Otp.TAG);
                tv_header.setText(getString(R.string.enter_otp));
                break;
        }
    }

    private void callfragment(Fragment fragment,String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.password_activity_container,fragment,tag).addToBackStack(tag).commit();
    }

    private void initViews() {
        mActivityreference=this;
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_reset_and_forget);
        circularProgressBar=(CircularProgressView)findViewById(R.id.progress_view);
         Toolbar toolbar=(Toolbar)findViewById(R.id.reset_forgot_toolbar);
        tv_header=(TextView)findViewById(R.id.txt_activty_header);
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }


    public void callOTPFragment(int i) {
        tv_header.setText(getString(otp));
        Fragment otp=new Otp();
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValues.CHANGE_NUMBER,i);
        otp.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.password_activity_container,otp,Otp.TAG).addToBackStack(Otp.TAG).commit();
    }

    @Override
    public void callResetFragment() {
        tv_header.setText(getString(R.string.txt_reset_pasword));
        ResetPassword resetPassword=new ResetPassword();
        getSupportFragmentManager().beginTransaction().replace(R.id.password_activity_container,resetPassword).addToBackStack(ResetPassword.TAG).commit();
    }

    @Override
    public void handleChangenumber(String changeNumber,String countryCode) {
        isChangeNumber=true;
       prepareforchangeNumber(changeNumber,countryCode);
    }


    @Override
    public void callForgotFragment(int changenumber) {
        if(changenumber==1){
            tv_header.setText(getString(R.string.txt_change_number));
        }else{
            tv_header.setText(getString(R.string.txt_forgot_password));
        }
        Fragment forgotPassword=new ForgotPassword();
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValues.CHANGE_NUMBER,changenumber);
        forgotPassword.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.password_activity_container,forgotPassword).addToBackStack(ForgotPassword.TAG).commit();
    }

    @Override
    public void handleResetpasswordResult(Bundle data) {
        this.finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        isForgot=false;
        isChangeNumber=false;
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
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStackImmediate();
            Fragment fragment=getCurrentFragment();
           if(fragment instanceof ForgotPassword){
               if(changeNumber==1) {
                   tv_header.setText(getResources().getString(R.string.txt_change_number));
               }else{
                   tv_header.setText(getResources().getString(R.string.txt_forgot_pasword));
               }
           }
            if(fragment instanceof ResetPassword){
                tv_header.setText(getResources().getString(R.string.txt_reset_pasword));
            }
            if(fragment instanceof Otp){
                tv_header.setText(getResources().getString(otp));
            }
        }else {
            finish();
        }
    }

    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                onBackPressed();
                break;
        }
    }

    @Override
    public void handleVerifyUser(String OTP, int forgotpassword) {
        if(isForgot){
            prepareVerifiedFORGOTUser(OTP);
        }else {
            prepareVerifiedUser(OTP);
        }

    }

    @Override
    public void handleResendOTP() {
        prepareResendOtp();

    }

    @Override
    public void handleForgotPassword(String mobilNumber, boolean changeNumber1) {
        isForgot=true;
        prepareforgotPasswordUser(mobilNumber);
    }

    @Override
    public void handleResetPassword(String oldpassword, String Password) {
        prepareresetPasswordUser(oldpassword,Password);
    }




    public void  prepareresetPasswordUser( String oldPassword,String newPassword) {
        String mobileNumber=oldPassword;
        if(mobileNumber.contains(" ")){
            String[] array=mobileNumber.split(" ");
            mobileNumber=array[1];
        }
        mobileNumber=mobileNumber.trim();
        String createUserUrl= WebAPIManager.getInstance().getresetPasswordUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER,mobileNumber);
        requestBody.addProperty(ConstantValues.USER_PASSWORD,newPassword);
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_RESET_PASSWORD);
    }

    public void  prepareforchangeNumber(String mobilenumber,String countryCode) {
        HandleForgotuser.getInstance().setMobileNumber(mobilenumber);
        mobilenumber=mobilenumber.trim();
        String createUserUrl= WebAPIManager.getInstance().getchangeNumberUrl();
        createUserUrl=createUserUrl+mobilenumber;
        createUserUrl=createUserUrl+"&"+ConstantValues.COUNTRY_CODE+"="+countryCode;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_CHANGENUMBER);
    }


    public void  prepareforgotPasswordUser( String number) {
        number=number.trim();
        String createUserUrl= WebAPIManager.getInstance().getforgotPasswordUrl();
        createUserUrl=createUserUrl+"phone_number="+number;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_FORGOT_PASSWORD);
    }

    public void  prepareResendOtp() {
        String mobileNumber=HandleForgotuser.getInstance().getMobileNumber();
        if(mobileNumber!=null) {
            if (mobileNumber.contains(" ")) {
                String[] array = mobileNumber.split(" ");
                mobileNumber = array[1];
            }
        }
        String createUserUrl = WebAPIManager.getInstance().getResenOTPdUrl();
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER,mobileNumber);
        callAPI(requestBody, createUserUrl, this, 60 * 1000, REQUEST_RESEND_OTP);
    }

    public void  prepareVerifiedUser( String otp) {
        otp=otp.replace("-","");
        String createUserUrl= WebAPIManager.getInstance().getVerifyUserUrl();
        createUserUrl=createUserUrl+"verification_code="+otp;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_VERIFY_USER);
    }

    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack callBack, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        String accessToken= User.getInstance().getAccesstoken();
        Ion.with(this)
                .load("PUT",createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN,accessToken)
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if(e!=null){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                       String value=String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            message=jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status){
                            case 403:
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
                                callBack.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    public void  prepareVerifiedFORGOTUser( String otp) {
        otp=otp.replace("-","");
        String mobileNumber=HandleForgotuser.getInstance().getMobileNumber();
        if(mobileNumber.contains(" ")){
            String[] array=mobileNumber.split(" ");
            mobileNumber=array[1];
        }
        String createUserUrl= WebAPIManager.getInstance().getVerifyUserUrl();
        createUserUrl=createUserUrl+"verification_code="+otp;
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty("password_otp",1);
        requestBody.addProperty("phone_number",mobileNumber);
        callAPIFORFORGOT(requestBody,createUserUrl,this,60*1000,REQUEST_VERIFY_USER);
    }


    private void callAPIFORFORGOT(JsonObject requestBody, String createUserUrl, final NetworkCallBack callBack, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("PUT",createUserUrl)
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if(e!=null){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value=String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            message=jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status){
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
                                callBack.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                circularProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }







    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode){
            case REQUEST_VERIFY_USER:
                ParseCreateUserresponse parseCreateUserresponse=new ParseCreateUserresponse();
                parseCreateUserresponse.execute(data.toString());
                break;
            case REQUEST_RESEND_OTP:
                ParseResendresponse parseResendresponse=new ParseResendresponse();
                parseResendresponse.execute(data.toString());
                break;
            case REQUEST_FORGOT_PASSWORD:
                ParseForgotresponse parseForgotresponse=new ParseForgotresponse();
                parseForgotresponse.execute(data.toString());
                break;
            case REQUEST_RESET_PASSWORD:
                ParseResetresponse parseResetresponse=new ParseResetresponse();
                parseResetresponse.execute(data.toString());
                break;
            case REQUEST_CHANGENUMBER:
                ParseResetresponse parseChangeNumber=new ParseResetresponse();
                parseChangeNumber.execute(data.toString());
                break;
        }

    }





    class ParseForgotresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message="",flag="";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String mobileNumber=jsonObject1.getString("phone_number");
                String country_code=jsonObject1.getString("country_code");
                message = jsonObject.getString("message");
                map.put("message", message);
                map.put(ConstantValues.USER_MOBILENUMBER, mobileNumber);
                map.put("country_code",country_code);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            HandleForgotuser.getInstance().setMobileNumber(hashMap.get("country_code")+" "+hashMap.get(ConstantValues.USER_MOBILENUMBER));
            callOTPFragment(0);
            Toast.makeText(ResetAndForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }






    class ParseResetresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message="",flag="",mobilenumber="";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String country_code=jsonObject1.getString("country_code");
                String phoneNumber=jsonObject1.getString("phone_number");
                map.put("country_code",country_code);
                map.put("phone_number",phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            if(isChangeNumber){
                HandleForgotuser.getInstance().setMobileNumber(hashMap.get("country_code")+" "+hashMap.get("phone_number"));
                callOTPFragment(1);
            }else {
                finish();
            }
            Toast.makeText(ResetAndForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }

    class ParseResendresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message;
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                map.put("message", message);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String countryCode=jsonObject1.getString("country_code");
                map.put("countryCode",countryCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            Toast.makeText(ResetAndForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }


    class ParseCreateUserresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("response");
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            if(isForgot) {
                callResetFragment();
            }else {
                User.getInstance().setVarified(true, ResetAndForgetPasswordActivity.this, true);
                callHomeActivity();
            }
            Toast.makeText(ResetAndForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }

    private void callHomeActivity() {
        Intent intent=new Intent(ResetAndForgetPasswordActivity.this,MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onStop() {
        isForgot=false;
        isChangeNumber=false;
        super.onStop();
    }

    @Override
    public void onError(String msg) {
    }
}
