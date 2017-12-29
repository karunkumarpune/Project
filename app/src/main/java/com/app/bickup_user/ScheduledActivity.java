package com.app.bickup_user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.adapter.SchedulAdapter;
import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.model.User;
import com.app.bickup_user.retrofit.APIService;
import com.app.bickup_user.retrofit.ApiUtils;
import com.app.bickup_user.retrofit.model.OnGoing;
import com.app.bickup_user.retrofit.model.Responses;
import com.app.bickup_user.utility.CommonMethods;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduledActivity extends AppCompatActivity implements View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener {


    private CircularProgressView circularProgressView;
    private String TAG= "ScheduledActivity";
    private ImageView imgBack;
    private CoordinatorLayout mCoordinatorLayout;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private APIService mAPIService;
    private RecyclerView recyclerView;
    private SchedulAdapter rideAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_scheduled);
        mAPIService = ApiUtils.getAPIService();
        initializeViews();
        initJsonParese();

    }


    private void initializeViews() {
        mActivityreference=this;
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator__activity_scheduled);
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        circularProgressView=findViewById(R.id.progress_view);
        recyclerView = findViewById(R.id.scheduled_recy);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        txtHeader.setText(getResources().getString(R.string.txt_schedule_delivery));
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
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


    @Override
    protected void onResume() {
        super.onResume();
        initializeViews();
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

    private void initJsonParese() {
        final String[] message = new String[1];
        circularProgressView.setVisibility(View.VISIBLE);
        mAPIService.getSchedul(User.getInstance().getAccesstoken()).enqueue(new Callback<OnGoing>() {
            @Override
            public void onResponse(Call<OnGoing> call, Response<OnGoing> response) {
                circularProgressView.setVisibility(View.GONE);
                int status = response.code();

                if (response.isSuccessful()) {
                    List<Responses> list = response.body().getResponses();
                    try {
                        rideAdapter = new SchedulAdapter(ScheduledActivity.this, (ArrayList<Responses>) list);
                        recyclerView.setAdapter(rideAdapter);
                    }catch (Exception e){}

                }
                if (status != 200) {
                    switch (status) {
                        case 422:
                            Toast.makeText(ScheduledActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(ScheduledActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(ScheduledActivity.this, message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 201:
                            Toast.makeText(ScheduledActivity.this, getResources().getString(R.string.txt_driver_201), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ScheduledActivity.this, message[0], Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OnGoing> call, Throwable t) {
                circularProgressView.setVisibility(View.GONE);
                if (t != null) {
                    Toast.makeText(ScheduledActivity.this, getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}
