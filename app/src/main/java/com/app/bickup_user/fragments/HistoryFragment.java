package com.app.bickup_user.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.app.bickup_user.adapter.OngoingRideAdapter;
import com.app.bickup_user.model.User;
import com.app.bickup_user.retrofit.APIService;
import com.app.bickup_user.retrofit.ApiUtils;
import com.app.bickup_user.retrofit.model.OnGoing;
import com.app.bickup_user.retrofit.model.Responses;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {
    private APIService mAPIService;
    private String TAG= "OnGoingFragment";
    private RecyclerView recyclerView;
    private OngoingRideAdapter rideAdapter;
    private CircularProgressView circularProgressView;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAPIService = ApiUtils.getAPIService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ongoing_deliverys, container, false);
        recyclerView = view.findViewById(R.id.delivery_recy);
        circularProgressView=view.findViewById(R.id.progress_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initJsonParese();
        return view;
    }
    private void initJsonParese() {
        final String[] message = new String[1];
        circularProgressView.setVisibility(View.VISIBLE);
        mAPIService.getHistoryRide(User.getInstance().getAccesstoken()).enqueue(new Callback<OnGoing>() {
            @Override
            public void onResponse(Call<OnGoing> call, Response<OnGoing> response) {
                circularProgressView.setVisibility(View.GONE);
                  int status = response.code();

                if (response.isSuccessful()) {
                    List<Responses> list = response.body().getResponses();
                    try {
                        rideAdapter = new OngoingRideAdapter(getActivity(), (ArrayList<Responses>) list);
                        recyclerView.setAdapter(rideAdapter);
                    }catch (Exception e){}

                }
                if (status != 200) {
                    switch (status) {
                        case 422:
                            Toast.makeText(getActivity(), message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(getActivity(), message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), message[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 201:
                            Toast.makeText(getActivity(), getResources().getString(R.string.txt_driver_201), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), message[0], Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OnGoing> call, Throwable t) {
                circularProgressView.setVisibility(View.GONE);
                if (t != null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}
