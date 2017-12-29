package com.app.bickup_user.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Activity activity;
    private String[] array;
    private LayoutInflater inflater;

    public ContactsAdapter(Activity activity, String[] array){
        this.array=array;
        this.activity=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_driver_contacts, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.driverName.setText(array[position]);
    }

    @Override
    public int getItemCount() {
        return (null != array ? array.length : 0);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView driverName;
        public ImageView checkBoximage;

        public MyViewHolder(View view) {
            super(view);
            driverName=(TextView)view.findViewById(R.id.driver_name);

        }
    }

}
