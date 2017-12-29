package com.app.bickup_user.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fluper-pc on 14/9/17.
 */

public class CommonMethods {
    private static final CommonMethods ourInstance = new CommonMethods();

    public static CommonMethods getInstance() {
        return ourInstance;
    }

    private CommonMethods() {
    }

    // Hide keyboard
    public void hideSoftKeyBoard(Activity activity){
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view=activity.getCurrentFocus();
        if(view!=null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

  // save data to preferences
    public void saveDataToPreferences(Context context, String preferenceName, HashMap<String,String> hashMapFordata){
        if(context!=null&&hashMapFordata!=null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
            SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();
            if(hashMapFordata.size()>0) {
                Set keys = hashMapFordata.keySet();
                for (Iterator i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    String value = (String) hashMapFordata.get(key);
                    preferenceEditor.putString(key,value);

                }
                preferenceEditor.commit();
            }

        }
    }


    // clear Preferences on session out
    public void clearSharePreferences(Context context, String preferenceName){
        if(context!=null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
            preferenceEditor.clear();
            preferenceEditor.commit();
        }
    }

    //Validate email address
    public boolean validateEmailAddress(String email){
        boolean isValid = false;
        String expression ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }



   // validate mobile number
    public boolean validateMobileNumber(String mobilenumber,int length){
        boolean isValid=false;
        if(mobilenumber!=null) {
            if (mobilenumber.length() >= length) {
                isValid = true;
            }
        }
        return isValid;
    }

   // validate edit text feild data
    public boolean validateEditFeild(String feilddata){
        boolean isValid=false;
        if(feilddata!=null) {
            if (feilddata.length() > 0) {
                isValid = true;
            }
        }
        return isValid;

    }

    //check Internet connection when activity gets open
    public boolean checkInterNetConnection(Context context){
        ConnectivityManager mConnectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworKInfo=mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected=mNetworKInfo!=null&&mNetworKInfo.isConnectedOrConnecting();
        return isConnected;
    }


}
