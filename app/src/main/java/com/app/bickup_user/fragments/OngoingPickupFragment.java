package com.app.bickup_user.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickup_user.GoodsActivity;
import com.app.bickup_user.R;
import com.app.bickup_user.TrackDriverActivity;


public class OngoingPickupFragment extends Fragment {


    private TextView textView1;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ongoing_pickup, container, false);
        RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.btn_container);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),GoodsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        textView = (TextView) view.findViewById(R.id.txt_track_driver);
        textView1 = (TextView) view.findViewById(R.id.track_driver_below);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrackDriverActivity.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrackDriverActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
