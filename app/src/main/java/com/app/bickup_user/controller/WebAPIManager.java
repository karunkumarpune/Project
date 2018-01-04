package com.app.bickup_user.controller;

import com.app.bickup_user.utility.ConstantValues;

/**
 * Created by fluper-pc on 29/10/17.
 */

public class WebAPIManager {
    private static final WebAPIManager ourInstance = new WebAPIManager();

    public static WebAPIManager getInstance() {
        return ourInstance;
    }

    private WebAPIManager() {
    }


    public String getCreateUserUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user";
        return  url;
    }

    public String getUserLoginUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/login";
        return  url;
    }

    public String getVerifyUserUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/verifyOtp?";
        return  url;
    }

    public String getResenOTPdUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/resendOtp";
        return  url;
    }
    public String getSocialLoginUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/social";
        return  url;
    }

    public String getSocialSignupUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/completeSignup";
        return  url;
    }

    public String getforgotPasswordUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/forgotPassword?";
        return  url;
    }

    public String getresetPasswordUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/resetPassword?";
        return  url;
    }
    public String getchangeNumberUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/changeNumber?phone_number=";
        return  url;
    }

    public String getimageUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user";
        return  url;
    }

    public String getMapDirectionUrl(){
        String url= "https://maps.googleapis.com/maps/api/directions/json?";
        return  url;
    }

    public String getSaveAddressUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/save_address";
        return  url;
    }
    public String getTypesGoods(){
        String url= ConstantValues.BASE_URL;
        url=url+"/get_goods_and_helper";
        return  url;
    }
    public String getFareDetailsUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/user/fare_details";
        return  url;
    }


    public static String get_url_Rides =ConstantValues.BASE_URL+"/ride";

    public static String get_url_Ride =ConstantValues.BASE_URL+"/ride";

    //Retrofit
    public static final String get_url_scheduledRide="/user/scheduledRide";

    public static final String get_url_historyRide ="/user/historyRide";

    public static final String get_url_ongoingRide ="/user/ongoingRide";

    public static final String get_url_statusUpdated ="/user/status";


  //Ion
    public static final String get_url_rateDriver =ConstantValues.BASE_URL+"/user/rateDriver";

    public static final String get_url_Cancel =ConstantValues.BASE_URL+"/user/ride/cancel";

    public static final String get_url_Reschedule =ConstantValues.BASE_URL+"/user/ride/reschedule";


    //      /user/status/{ride_id}

}
