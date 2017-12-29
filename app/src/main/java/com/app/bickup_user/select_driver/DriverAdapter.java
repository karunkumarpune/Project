package com.app.bickup_user.select_driver;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    private Activity context;
    private LayoutInflater inflater;
    private ArrayList<Driver_Model> filterList;
    private ArrayList<Driver_Model> list;

    public DriverAdapter(Activity context, ArrayList<Driver_Model> list) {

        this.list=list;
        this.filterList =new  ArrayList<>();
        this.filterList.addAll(this.list);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driver_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Driver_Model listItem = filterList.get(position);
        holder.tv_name.setText(listItem.getName());
        Ion.with(holder.image_driver)
                .placeholder(R.drawable.driver)
                .load(listItem.getAvatar());
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView image_driver;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            image_driver = view.findViewById(R.id.image_driver);

        }


    }
    public void filter(final String text) {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                filterList.clear();
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(list);
                } else {
                    for (Driver_Model item : list) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getName().toLowerCase().contains(text.toLowerCase())) {
                            filterList.add(item);
                        }
                    }
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
