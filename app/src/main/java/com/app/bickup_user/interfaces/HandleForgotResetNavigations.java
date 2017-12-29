package com.app.bickup_user.interfaces;

import android.os.Bundle;

/**
 * Created by fluper-pc on 28/9/17.
 */

public interface HandleForgotResetNavigations {


    public void  callResetFragment();


    public void  handleChangenumber(String changeNumber,String countryCode);
    public void handleVerifyUser(String OTP,int changeNumber);
    public void handleResendOTP();
    public void handleForgotPassword(String mobilNumber,boolean changeNumber);
    public void handleResetPassword(String oldpassword,String  Password);

  /*  public void handleResetPassword(String newpassword);
    public void handleForgotPassword(String phoneNumber);
    public void handleChangeNumber(String changeNumber);*/

    public void  callForgotFragment(int changeNumber);
    public void  handleResetpasswordResult(Bundle data);


}
