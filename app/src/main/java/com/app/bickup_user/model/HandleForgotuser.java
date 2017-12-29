package com.app.bickup_user.model;

/**
 * Created by fluper-pc on 31/10/17.
 */

public class HandleForgotuser {
    private String accesstoken=null;
    private String mobileNumber=null;

    private static final HandleForgotuser ourInstance = new HandleForgotuser();


    public static HandleForgotuser getInstance() {
        return ourInstance;
    }

    private HandleForgotuser() {
    }


    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
