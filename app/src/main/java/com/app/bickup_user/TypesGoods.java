package com.app.bickup_user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.adapter.ContactsAdapter;
import com.app.bickup_user.adapter.TypesGoodsAdapter;
import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.model.GoodsAndHelper;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;

public class TypesGoods extends AppCompatActivity implements InternetConnectionBroadcast.ConnectivityRecieverListener, View.OnClickListener {

    private boolean mIsConnected=false;
    private Context mActivityreference;
    private Snackbar snackbar;
    private RecyclerView listViewTypesGoods;
    private CoordinatorLayout mCoordinatorLayout;

    private TextView tv_header;

    private ImageView imgBack;
    private String[] array={"Single Bed","Refrigerator","Single Bed","Refrigerator","Single Bed","Refrigerator","Single Bed","Refrigerator","Single Bed","Refrigerator","Single Bed","Refrigerator"};
    private ImageView tickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_types_goods);
        initializeViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        if(getIntent()!=null) {
            if(getIntent().getIntExtra(TrackDriverActivity.OPENTYPESGOODS,0)==0) {
                GoodsAndHelper goodsAndHelper=getIntent().getParcelableExtra(ConstantValues.GOODSDETAILS);
                tv_header.setText(getResources().getString(R.string.txt_types_of_goods));
                TypesGoodsAdapter typesGoodsAdapter=new TypesGoodsAdapter(this,goodsAndHelper.getGoods());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
                listViewTypesGoods.setLayoutManager(mLayoutManager);
                listViewTypesGoods.setItemAnimator(new DefaultItemAnimator());
                listViewTypesGoods.setAdapter(typesGoodsAdapter);
            }else {
                tv_header.setText(getResources().getString(R.string.txt_contacts_number));
                ContactsAdapter typesGoodsAdapter=new ContactsAdapter(this,array);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
                listViewTypesGoods.setLayoutManager(mLayoutManager);
                listViewTypesGoods.setItemAnimator(new DefaultItemAnimator());
                listViewTypesGoods.setAdapter(typesGoodsAdapter);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    private void initializeViews() {
        mActivityreference=this;
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_type_goods);
        tv_header=(TextView)findViewById(R.id.txt_activty_header);

        listViewTypesGoods=(RecyclerView) findViewById(R.id.list_types_goods);
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        tickImage=(ImageView)findViewById(R.id.img_tick_toolbar);
        tickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tickImage.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
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
        snackbar = Snackbar.make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
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
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
              finish();
                break;
        }
    }
}
