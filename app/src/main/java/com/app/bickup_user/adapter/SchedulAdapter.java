package com.app.bickup_user.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bickup_user.R;
import com.app.bickup_user.retrofit.APIService;
import com.app.bickup_user.retrofit.ApiUtils;
import com.app.bickup_user.retrofit.model.Responses;
import com.app.bickup_user.retrofit.model.Ride;
import com.app.bickup_user.utility.ConstantValues;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulAdapter extends RecyclerView.Adapter<SchedulAdapter.MyViewHolder> {
    private APIService mAPIService;
    private Context context;
    private LayoutInflater inflater;
    private List<Responses> list;


    public SchedulAdapter(Activity activity, ArrayList<Responses> list){
        mAPIService = ApiUtils.getAPIService();
        this.list=list;
        this.context=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_deliveries_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Responses res = list.get(position);

          holder.today_txt.setText(res.getDate());
          holder.tvNoOfDeliveries.setText("No of scheduled: "+String.valueOf(res.getRide().size()));



//----------------------------------Runtime Inflater------------------------------

        LayoutInflater layoutInflater;
        View view = null;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.llRootLayout.removeAllViews();
          for (final Ride ride : res.getRide()) {
            view = layoutInflater.inflate(R.layout.row_scheduled_layout, holder.llRootLayout, false);
            RoundedImageView user_image_row_list = view.findViewById(R.id.user_image_row_list);
            TextView edt_pickup_location = view.findViewById(R.id.edt_pickup_location);
            TextView edt_drop_location = view.findViewById(R.id.edt_drop_location);
            TextView row_date_schedule = view.findViewById(R.id.row_date_schedule);
            TextView row_time_schedule = view.findViewById(R.id.row_time_schedule);
            TextView txt_recshedul = view.findViewById(R.id.txt_recshedul);
            TextView txt_cancel_schedule = view.findViewById(R.id.txt_cancel_schedule);

              String time = DateUtils.formatDateTime(context, Long.parseLong(ride.getTimestamp()), DateUtils.FORMAT_SHOW_TIME);


              try {
                  SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                  Date dateObj = sdf.parse(time);
                  System.out.println(dateObj);
                  System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
              } catch (final ParseException e) {
                  e.printStackTrace();
              }


              edt_pickup_location.setText(ride.getPickupLocationAddress());
              edt_drop_location.setText(ride.getDropLocationAddress());
              row_date_schedule.setText(ride.getDate());
              row_time_schedule.setText(time);

            //--------------Avatar
            if (ride.getProfileImage() != null) {
                Ion.with(user_image_row_list)
                        .placeholder(R.drawable.driver)
                        .error(R.drawable.driver)
                        .load(ConstantValues.BASE_URL + "/" + ride.getProfileImage().getImageUrl());
            }
            holder.llRootLayout.addView(view);

        }
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llRootLayout;
        private TextView today_txt;
        private TextView tvNoOfDeliveries;

        public MyViewHolder(View view) {
            super(view);
            today_txt= view.findViewById(R.id.tvDate);
            tvNoOfDeliveries= view.findViewById(R.id.tvNoOfDeliveries);
            llRootLayout = view.findViewById(R.id.llRootLayout);

        }
    }

}
