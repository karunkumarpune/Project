package com.app.bickup_user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.fragments.BookingDetailsFragment;
import com.app.bickup_user.fragments.BookingDetailsFragmentKotlin;
import com.app.bickup_user.fragments.GoodsDetailsFragments;
import com.app.bickup_user.interfaces.HandlerGoodsNavigations;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;

import java.util.ArrayList;


public class GoodsActivity extends AppCompatActivity  implements HandlerGoodsNavigations,View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener {

    private TextView txtHeader;
    private RelativeLayout rlContainer;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_goods);
        initializeViews();
        addGoodsdetailsFragments();
    }

    private void addGoodsdetailsFragments() {
        txtHeader.setText(getString(R.string.txt_goods_details));
        GoodsDetailsFragments goodsDetailsFragments=new GoodsDetailsFragments();
        getSupportFragmentManager().beginTransaction().add(R.id.goods_activity_container,goodsDetailsFragments).addToBackStack(GoodsDetailsFragments.TAG).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }
    private void checkInternetconnection() {
        mIsConnected = CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if (mIsConnected) {
            if (snackbar != null) {
                snackbar.dismiss();

            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    public void showSnackBar(String mString) {
       /* snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
    }
    private void initializeViews() {
        mActivityreference=this;
        ImageView imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener((View.OnClickListener) this);

        mTypefaceRegular= Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setTypeface(mTypefaceRegular);
        rlContainer=(RelativeLayout)findViewById(R.id.goods_activity_container);
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_activity_goods);
    }


    @Override
    public void callBookingDetailsFragment(ArrayList<Bitmap> listImages) {
        BookingDetailsFragmentKotlin bookingDetailsFragments=new BookingDetailsFragmentKotlin();
        bookingDetailsFragments.setImagelist(listImages);
        txtHeader.setText(getResources().getString(R.string.txt_view_details));
        getSupportFragmentManager().beginTransaction().replace(R.id.goods_activity_container,bookingDetailsFragments).addToBackStack(null).commit();
    }

    @Override
    public void callGoodsFragment() {
        GoodsDetailsFragments goodsDetailsFragments=new GoodsDetailsFragments();
        txtHeader.setText(getResources().getString(R.string.txt_goods_details));
        getSupportFragmentManager().beginTransaction().replace(R.id.goods_activity_container,goodsDetailsFragments).addToBackStack(GoodsDetailsFragments.TAG).commit();
    }

    @Override
    public void callTrackDriverActivity() {
        Intent intent=new Intent(this,TrackDriverActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStackImmediate();
            Fragment fragment=getCurrentFragment();
            if(fragment instanceof BookingDetailsFragment){
                txtHeader.setText(getResources().getString(R.string.txt_view_details));
            }
            if(fragment instanceof GoodsDetailsFragments){
                txtHeader.setText(getResources().getString(R.string.txt_goods_details));
            }

        }else {
            finish();
        }
    }

    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }
}