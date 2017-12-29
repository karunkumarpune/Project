package com.app.bickup_user.interfaces;

import org.json.JSONException;

/**
 * Created by fluper-pc on 18/9/17.
 */

public interface HandleLoginSignUpNavigation {
    public void performSignIn(String mobileNumber, String password, String countryCode);
    public void performGoogleSignIn(boolean isSignup);
    public void performFacebookSignIn(boolean isSignup);
    public void performSocialSignUp();
    public void performSignUp(String firstname,String lastname,String mobileNumber,String email,String password,String countryCode) throws JSONException;
    public void callSignupFragment();
    public void callSigninFragment();
    public void callForgotAndReset(int chechForgotReset,int changenumber);
    public void callMainActivity();

}
