package com.app.bickup_user.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.bickup_user.model.HandleForgotuser;
import com.app.bickup_user.model.User;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;


public class Otp extends Fragment implements View.OnClickListener {

    public static String TAG=Otp.class.getSimpleName();
    private ResetAndForgetPasswordActivity mActivityReference;
    private EditText edtOTP;
    private TextView txtHeader;
    private TextView txtMobileNumber;
    private TextView txtResend;
    private Button btnSubmit;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity mActivity;
    private int changeNumber;


    public Otp() {
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
         View view=inflater.inflate(R.layout.fragment_otp, container, false);
        initializeviews(view);
        return view;
    }

    private void initializeviews(View view) {
        txtMobileNumber=(TextView)view.findViewById(R.id.txt_mobile_otp);
        if(getArguments()!=null) {
            changeNumber = getArguments().getInt(ConstantValues.CHANGE_NUMBER, 0);
        }

        if(ResetAndForgetPasswordActivity.isChangeNumber||ResetAndForgetPasswordActivity.isForgot){
            txtMobileNumber.setText(HandleForgotuser.getInstance().getMobileNumber());
        }else {
            txtMobileNumber.setText(User.getInstance().getCountryCode()+" "+User.getInstance().getMobileNumber());
        }

        if(changeNumber==1){
            view.findViewById(R.id.btn_change_number).setVisibility(View.VISIBLE);
            Button button=view.findViewById(R.id.btn_change_number);
            button.setTypeface(mTypefaceRegular);
            view.findViewById(R.id.btn_change_number).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivityReference.callForgotFragment(1);
                }
            });
        }
        RelativeLayout container=(RelativeLayout)view.findViewById(R.id.otp_container);
        edtOTP=(EditText)view.findViewById(R.id.edt_mobile_otp);
        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtOTP.getText().toString();
                int textlength = edtOTP.getText().length();
                if(text.endsWith("-"))
                    return;
                if(textlength == 4)
                {
                    edtOTP.setText(new StringBuilder(text).insert(text.length()-1, "-").toString());
                    edtOTP.setSelection(edtOTP.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtHeader=(TextView)view.findViewById(R.id.txt_header_otp);

        txtResend=(TextView)view.findViewById(R.id.txt_resend_Otp);
        btnSubmit=(Button)view.findViewById(R.id.btn_submit_otp);

        btnSubmit.setOnClickListener(this);
        txtResend.setOnClickListener(this);

        txtResend.setTypeface(mTypefaceRegular);
        txtMobileNumber.setTypeface(mTypefaceBold);
        txtHeader.setTypeface(mTypefaceRegular);
        btnSubmit.setTypeface(mTypefaceRegular);
        edtOTP.setTypeface(mTypefaceRegular);

        setUIToHideKeyBoard(container);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(ResetAndForgetPasswordActivity) context;
        mActivity=(Activity)context;

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
                    if(mActivity!=null) {
                       // CommonMethods.getInstance().hideSoftKeyBoard(mActivity);
                    }
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

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtOTP.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_otp), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        HandleForgotResetNavigations handleForgotResetNavigations=mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_submit_otp:
                if(validateFields()) {
                    handleForgotResetNavigations.handleVerifyUser(edtOTP.getText().toString(),changeNumber);
                       /* Intent intent=new Intent(mActivityReference, DriverActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivityReference.startActivity(intent);*/
                }
                break;
            case R.id.txt_resend_Otp:
                handleForgotResetNavigations.handleResendOTP();
                break;
        /*    case R.id.btn_change_number:
                handleForgotResetNavigations.handleResendOTP();
                break;*/
        }
    }
}
