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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.app.bickup_user.ResetAndForgetPasswordActivity;
import com.app.bickup_user.interfaces.HandleForgotResetNavigations;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import static com.app.bickup_user.R.id.btn_submit_on_changeNumber;


public class ForgotPassword extends Fragment implements View.OnClickListener {

    private EditText edtMobileNumber;
    private ResetAndForgetPasswordActivity mActivityReference;
    private TextView txtheading;
    private Button btnSubmit;
    private Activity mActivity;



    public static String TAG=ForgotPassword.class.getSimpleName();
    private RelativeLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private int changeNumber;
    private TextView txtCountryCode;
    private String countryCode="+91";

    public ForgotPassword() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTypefaceRegular= Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_forgot_password, container, false);
        getData();
        intitializeViews(view);
        return view;
    }

    private void getData() {
        if(getArguments()!=null){
            changeNumber=getArguments().getInt(ConstantValues.CHANGE_NUMBER,0);
        }
    }

    private void intitializeViews(View view) {
        mActivity=getActivity();
        txtheading=(TextView)view.findViewById(R.id.txt_forgot_heading) ;
        btnSubmit=(Button)view.findViewById(R.id.btn_submit_on_forgot);
        //setUIToHideKeyBoard(container);
        btnSubmit.setOnClickListener(this);

        RelativeLayout rl_changeNumber=(RelativeLayout)view.findViewById(R.id.rl_change_number);
        RelativeLayout rl_forgot=(RelativeLayout)view.findViewById(R.id.rl_forgot_password);
        if(changeNumber==1){
            rl_changeNumber.setVisibility(View.VISIBLE);
            rl_forgot.setVisibility(View.GONE);
            txtCountryCode=(TextView)view.findViewById(R.id.edt_country_code) ;
            edtMobileNumber=(EditText)view.findViewById(R.id.edt_mobile_number_signup);
            view.findViewById(btn_submit_on_changeNumber).setOnClickListener(this);
        }else {
            rl_changeNumber.setVisibility(View.GONE);
            rl_forgot.setVisibility(View.VISIBLE);
            txtCountryCode=(TextView)view.findViewById(R.id.edt_country_code_forgot) ;
            edtMobileNumber=(EditText)view.findViewById(R.id.edt_mobile_forgot);
        }
        edtMobileNumber.setTypeface(mTypefaceRegular);
        txtheading.setTypeface(mTypefaceRegular);
        btnSubmit.setTypeface(mTypefaceRegular);
        txtCountryCode.setTypeface(mTypefaceRegular);
        txtCountryCode.setOnClickListener(this);

    }

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtMobileNumber.getText().toString())){
            Toast.makeText(mActivityReference, mActivity.getResources().getString(R.string.txt_vaidate_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(),6)){
            Toast.makeText(mActivityReference, mActivity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference= (ResetAndForgetPasswordActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    CommonMethods.getInstance().hideSoftKeyBoard(mActivityReference);
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

    public  void  openCountryCodeDialog(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                countryCode=dialCode;
                txtCountryCode.setText(dialCode);
            }
        });
        picker.show(mActivityReference.getSupportFragmentManager(), "COUNTRY_PICKER");

    }


    @Override
    public void onClick(View view) {
        HandleForgotResetNavigations handleForgotResetNavigations= mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_submit_on_forgot:
                if(validateFields()) {
                    handleForgotResetNavigations.handleForgotPassword(edtMobileNumber.getText().toString(),true);
                }
                break;
            case R.id.edt_country_code:
            case R.id.edt_country_code_forgot:
                openCountryCodeDialog();
                break;
            case R.id.btn_submit_on_changeNumber:
                if(validateFields()) {
                    handleForgotResetNavigations.handleChangenumber(edtMobileNumber.getText().toString(), countryCode);
                }
                break;
        }
    }
}
