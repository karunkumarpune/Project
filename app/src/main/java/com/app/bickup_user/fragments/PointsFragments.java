package com.app.bickup_user.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bickup_user.R;
import com.app.bickup_user.utility.ConstantValues;


public class PointsFragments extends Fragment {


    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_shopkeeper_main, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        TextView txtBookPickup,txtSilver,txtYourEarned,txtvalueEarned,txtYourTotalBooking,txtValuetotalBooking,txtBronze,txtSilve,txtGold,txtReedme;

        txtBookPickup=(TextView)view.findViewById(R.id.txt_book_pickup);
        txtSilver=(TextView)view.findViewById(R.id.txt_silver);
        txtYourEarned=(TextView)view.findViewById(R.id.yout_earned_dhz);
        txtvalueEarned=(TextView)view.findViewById(R.id.txt_earned_value);

        txtYourTotalBooking=(TextView)view.findViewById(R.id.label_total_booing);
        txtValuetotalBooking=(TextView)view.findViewById(R.id.value_total_booing);

        txtSilve=(TextView)view.findViewById(R.id.txt_silver_bottom);
        txtBronze=(TextView)view.findViewById(R.id.txt_bronze_bottom);
        txtGold=(TextView)view.findViewById(R.id.txt_gold_bottom);
        txtReedme=(TextView)view.findViewById(R.id.txt_reedme);

        txtBookPickup.setTypeface(mTypefaceRegular);
        txtSilver.setTypeface(mTypefaceRegular);
        txtYourEarned.setTypeface(mTypefaceRegular);
        txtvalueEarned.setTypeface(mTypefaceBold);
        txtYourTotalBooking.setTypeface(mTypefaceRegular);
        txtValuetotalBooking.setTypeface(mTypefaceBold);
        txtSilve.setTypeface(mTypefaceRegular);
        txtBronze.setTypeface(mTypefaceRegular);
        txtGold.setTypeface(mTypefaceRegular);
        txtReedme.setTypeface(mTypefaceRegular);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
