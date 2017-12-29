package com.app.bickup_user.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.LoginActivity;
import com.app.bickup_user.R;
import com.app.bickup_user.interfaces.GetSocialLoginResultInterface;
import com.app.bickup_user.interfaces.HandleLoginSignUpNavigation;
import com.app.bickup_user.model.User;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;


public class LoginFragment extends Fragment implements GetSocialLoginResultInterface, View.OnClickListener {


    LoginActivity mActivityReference;
    Activity activity;
    private EditText edtMobileNumber;
    private EditText edtPassword;
    private Button  btnLogin;
    private ImageButton btnfacebook;
    private ImageButton btnGoogle;
    private TextView tvForgotPassword;
    private TextView tvSignUP;
    private LinearLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private TextView edtCoutryCode;
    private String countryCode="+91";


    public LoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mTypefaceRegular=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        container=(LinearLayout)view.findViewById(R.id.container_login);

        edtMobileNumber =(EditText)view.findViewById(R.id.edt_mobile_login);
        edtPassword =(EditText)view.findViewById(R.id.edt_password_login);
        btnLogin=(Button)view.findViewById(R.id.btn_login);
        btnfacebook= (ImageButton)view.findViewById(R.id.btn_facebook_login);
        btnGoogle= (ImageButton)view.findViewById(R.id.btn_google_login);
        tvForgotPassword= (TextView)view.findViewById(R.id.txt_forgot_password_login);
        tvSignUP=(TextView)view.findViewById(R.id.txt_signup_login);
        edtCoutryCode=(TextView)view.findViewById(R.id.edt_country_code);
        edtCoutryCode.setOnClickListener(this);

        tvSignUP.setTypeface(mTypefaceRegular);
        tvForgotPassword.setTypeface(mTypefaceRegular);
        btnLogin.setTypeface(mTypefaceRegular);
        edtMobileNumber.setTypeface(mTypefaceRegular);
        edtPassword.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
        btnLogin.setOnClickListener(this);
        tvSignUP.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnfacebook.setOnClickListener(this);

        tvSignUP.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

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

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(),6)){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }


        if(!CommonMethods.getInstance().validateEditFeild(edtPassword.getText().toString())){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(edtPassword.getText().toString().length()<8){
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password_strength), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

    @Override
    public void onResume() {
        if(edtMobileNumber!=null&&edtPassword!=null){
          //  edtMobileNumber.setText("");
          //  edtPassword.setText("");
        }
        super.onResume();
    }

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
        HandleLoginSignUpNavigation mHandleLoginSignUpNavigation =mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_login:
                if(validateFields()) {
                    mHandleLoginSignUpNavigation.performSignIn(edtMobileNumber.getText().toString().trim(),edtPassword.getText().toString().trim(),countryCode.trim());
                }
                break;
            case R.id.txt_signup_login:
                mHandleLoginSignUpNavigation.callSignupFragment();
                break;
            case R.id.txt_forgot_password_login:
                mHandleLoginSignUpNavigation.callForgotAndReset(ConstantValues.FORGOT_PASSWORD,0);
                break;
            case R.id.btn_google_login:
                mHandleLoginSignUpNavigation.performGoogleSignIn(true);
                break;
            case R.id.btn_facebook_login:
                mHandleLoginSignUpNavigation.performFacebookSignIn(true);
                break;
            case R.id.edt_country_code:
               openCountryCodeDialog();
                break;

        }
    }
}
