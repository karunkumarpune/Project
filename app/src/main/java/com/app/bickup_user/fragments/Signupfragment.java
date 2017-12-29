package com.app.bickup_user.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.LoginActivity;
import com.app.bickup_user.R;
import com.app.bickup_user.interfaces.GetSocialLoginResultInterface;
import com.app.bickup_user.interfaces.HandleLoginSignUpNavigation;
import com.app.bickup_user.model.Socialuser;
import com.app.bickup_user.model.User;
import com.app.bickup_user.push_notification.SharedPrefManager;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;


public class Signupfragment extends Fragment implements GetSocialLoginResultInterface, View.OnClickListener {

    private LoginActivity mActivityReference;
    private Activity activity;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtMobileNumber;
    private EditText edtChoosePassword;
    private EditText edtConfirmPassword;
    private EditText edtEmailID;
    private TextView edtCoutryCode;
    private Button btnSignUP;
    private ImageButton btnFacebook;
    private ImageButton btnGoogle;
    private TextView txtSignIn;
    private LinearLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private ImageView imgTermAndCondition;
    private String countryCode="+91";
    private int socialLogin;

    public Signupfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
        if (token != null) {
            GloableVariable.Tag_Is_device_token=token;
            Log.d("Tag_Is_device_token : ",GloableVariable.Tag_Is_device_token);
        } else {
            // GloableVariable.Tag_Is_device_token="fWQKCxXenHA:APA91bFJT1YJZgEaME5Co75b_cR1scI9NtrcjFzja8pF-oc0Fiw5xcXGIO-O2-Ak7lwYJAi6i8yEiSVjFRQFa_HH7w58Gxlp4132r3jl3XexxAbgYu93Fs0D3h7sVUvz_bfbv61HWAIs";

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signup, container, false);
        InitializeViews(view);
        getData(view);
        return view;
    }

    private void getData(View view) {
        if(getArguments()!=null){
             socialLogin=getArguments().getInt("Social");
            if(socialLogin==LoginActivity.REQUEST_SOCIAL_LOGIN){
                edtFirstname.setText(Socialuser.getInstance().getFirstName());
                edtLastname.setText(Socialuser.getInstance().getLastName());
                edtEmailID.setText(Socialuser.getInstance().getEmail());
                if(Socialuser.getInstance().getEmail()!=null){
                    edtEmailID.setEnabled(false);
                }
                view.findViewById(R.id.text_choose_password_signup).setVisibility(View.GONE);
                view.findViewById(R.id.rl_social_signup).setVisibility(View.GONE);
                view.findViewById(R.id.text_confirm_password).setVisibility(View.GONE);

            }
        }
    }

    private void InitializeViews(View view) {
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        container=(LinearLayout)view.findViewById(R.id.container_signup);
        edtFirstname=(EditText)view.findViewById(R.id.edt_first_name_signup);
        edtLastname=(EditText)view.findViewById(R.id.edt_last_name_signup);
        edtMobileNumber=(EditText)view.findViewById(R.id.edt_mobile_number_signup);
        edtChoosePassword=(EditText)view.findViewById(R.id.edt_choose_password_signup);
        edtConfirmPassword=(EditText)view.findViewById(R.id.edt_confirm_password_signup);
        edtEmailID=(EditText)view.findViewById(R.id.edt_email_signup);
        edtCoutryCode=(TextView)view.findViewById(R.id.edt_country_code);
        /*view.findViewById(R.id.edt_country_code_input_layout).setOnClickListener(this);*/
        view.findViewById(R.id.edt_country_code).setOnClickListener(this);
      /*  view.findViewById(R.id.edt_country_code).setOnClickListener(this);*/

        btnSignUP=(Button)view.findViewById(R.id.btn_submit_signup);
        btnFacebook=(ImageButton) view.findViewById(R.id.btn_facebook_signup);
        btnGoogle=(ImageButton) view.findViewById(R.id.btn_google_signup);
        txtSignIn=(TextView) view.findViewById(R.id.txt_signin_signup);
        imgTermAndCondition=(ImageView)view.findViewById(R.id.tick_image_signup);
        imgTermAndCondition.setTag(R.drawable.de_checkbox);

        txtSignIn.setOnClickListener(this);


        btnSignUP.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        edtCoutryCode.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        imgTermAndCondition.setOnClickListener(this);



        edtFirstname.setTypeface(mTypefaceRegular);
        edtLastname.setTypeface(mTypefaceRegular);
        edtMobileNumber.setTypeface(mTypefaceRegular);
        edtChoosePassword.setTypeface(mTypefaceRegular);
        edtEmailID.setTypeface(mTypefaceRegular);
        edtConfirmPassword.setTypeface(mTypefaceRegular);
        btnSignUP.setTypeface(mTypefaceRegular);
        txtSignIn.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
    }


    private boolean validateForSocialFields() {
        int tag= (int) imgTermAndCondition.getTag();
        if(!CommonMethods.getInstance().validateEditFeild(edtFirstname.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_first_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEditFeild(edtLastname.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_last_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(),6)){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEmailAddress(edtEmailID.getText().toString().trim())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_emailID), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tag==R.drawable.de_checkbox){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_term_and_conditions), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private boolean validateFields() {
        int tag= (int) imgTermAndCondition.getTag();
        if(!CommonMethods.getInstance().validateEditFeild(edtFirstname.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_first_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEditFeild(edtLastname.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_last_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(),6)){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEmailAddress(edtEmailID.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_emailID), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!CommonMethods.getInstance().validateEditFeild(edtChoosePassword.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(edtChoosePassword.getText().toString().length()<8){
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password_strength), Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        if(!CommonMethods.getInstance().validateEditFeild(edtConfirmPassword.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_confirm_password), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!edtChoosePassword.getText().toString().equalsIgnoreCase(edtConfirmPassword.getText().toString())){
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_confirm_password_and_password), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (tag==R.drawable.de_checkbox){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_term_and_conditions), Toast.LENGTH_SHORT).show();
           return false;
        }

        return true;
    }

    public  void  openCountryCodeDialog(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                  picker.dismiss();
                  countryCode=dialCode;
                  edtCoutryCode.setText(dialCode);
            }
        });
        picker.show(mActivityReference.getSupportFragmentManager(), "COUNTRY_PICKER");

    }



   /* public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(LoginActivity)context;
        activity=(Activity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void getLoginresult(User mUser) {

    }


    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                   CommonMethods.getInstance().hideSoftKeyBoard(mActivityReference);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUIToHideKeyBoard(innerView);
            }
        }

    }


    @Override
    public void onClick(View view) {
        HandleLoginSignUpNavigation handleLoginSignUpNavigation =mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_submit_signup:
                if(socialLogin==LoginActivity.REQUEST_SOCIAL_LOGIN){
                    if(validateForSocialFields()){
                        Socialuser.getInstance().setFirstName(edtFirstname.getText().toString().trim());
                        Socialuser.getInstance().setLastName(edtLastname.getText().toString().trim());
                        Socialuser.getInstance().setEmail(edtEmailID.getText().toString().trim());
                        Socialuser.getInstance().setMobileNumber(edtMobileNumber.getText().toString());
                        Socialuser.getInstance().setCountryCode(countryCode);
                        mActivityReference.performSocialSignUp();
                    }
                }else {
                    if(validateFields()) {
                        // mActivityReference.callForgotAndReset(ConstantValues.OTP,1);
                        mActivityReference.performSignUp(edtFirstname.getText().toString(),edtLastname.getText().toString(),edtMobileNumber.getText().toString(),edtEmailID.getText().toString(),edtConfirmPassword.getText().toString(),countryCode);
                    }
                }
                break;
            case R.id.txt_signin_signup:
                handleLoginSignUpNavigation.callSigninFragment();
                break;
            case R.id.btn_google_signup:
                handleLoginSignUpNavigation.performGoogleSignIn(false);
                break;
            case R.id.btn_facebook_signup:
                handleLoginSignUpNavigation.performFacebookSignIn(false);
                break;
            case R.id.tick_image_signup:
                int tag= (int) imgTermAndCondition.getTag();
               if (tag==R.drawable.de_checkbox){
                   setImageAndTag(R.drawable.ac_checkbox);
               }else{
                   if (tag==R.drawable.ac_checkbox){
                       setImageAndTag(R.drawable.de_checkbox);
                   }
               }
                break;
            case R.id.edt_country_code:
                openCountryCodeDialog();
                break;
        }
    }

    private void setImageAndTag(int id) {
        imgTermAndCondition.setImageResource(id);
        imgTermAndCondition.setTag(id);
    }



}
