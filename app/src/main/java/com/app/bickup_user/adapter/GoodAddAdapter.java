package com.app.bickup_user.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bickup_user.R;
import com.app.bickup_user.model.GoodsAddModel;

import java.util.ArrayList;


public class GoodAddAdapter extends RecyclerView.Adapter<GoodAddAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<GoodsAddModel> list;

    public GoodAddAdapter(Activity activity, ArrayList<GoodsAddModel> list) {
        this.activity = activity;
        this.list = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView option;

        public MyViewHolder(View view) {
            super(view);
            option = view.findViewById(R.id.tv_goods);

        }
    }


    @Override
    public GoodAddAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_good_option, parent, false);
        return new GoodAddAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoodAddAdapter.MyViewHolder holder, int position) {
        GoodsAddModel pos = list.get(position);
        holder.option.setText(pos.getName());
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}


