package com.app.bickup_user.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.R;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.model.User;
import com.app.bickup_user.retrofit.APIService;
import com.app.bickup_user.retrofit.ApiUtils;
import com.app.bickup_user.retrofit.model.Responses;
import com.app.bickup_user.retrofit.model.Ride;
import com.app.bickup_user.utility.ConstantValues;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OngoingRideAdapter extends RecyclerView.Adapter<OngoingRideAdapter.MyViewHolder> {
    private APIService mAPIService;
    private Context context;
    private LayoutInflater inflater;
    private List<Responses> list;
    private String message;


    public OngoingRideAdapter(Activity activity, ArrayList<Responses> list){
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
          holder.tvNoOfDeliveries.setText("No of deliveries: "+String.valueOf(res.getRide().size()));



//----------------------------------Runtime Inflater------------------------------




        LayoutInflater layoutInflater;
        View view = null;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.llRootLayout.removeAllViews();
          for (final Ride ride : res.getRide()) {
            view = layoutInflater.inflate(R.layout.ongoing_deliveries, holder.llRootLayout, false);
            RoundedImageView user_image_row_list = view.findViewById(R.id.user_image_row_list);
            TextView row_user_name = view.findViewById(R.id.row_user_name);
            TextView edt_pickup_location = view.findViewById(R.id.edt_pickup_location);
            TextView edt_drop_location = view.findViewById(R.id.edt_drop_location);
            TextView row_apx_fare = view.findViewById(R.id.row_apx_fare);
            TextView txt_track_driver = view.findViewById(R.id.txt_track_driver);

            //--------------Avatar
            if (ride.getProfileImage() != null) {
                Ion.with(user_image_row_list)
                        .placeholder(R.drawable.driver)
                        .error(R.drawable.driver)
                        .load(ConstantValues.BASE_URL + "/" + ride.getProfileImage().getImageUrl());
            }
            if (ride.getName() == null) {
                row_user_name.setText("Driver Name");
            } else row_user_name.setText(ride.getName());
            //------------------------
            edt_pickup_location.setText(String.valueOf(ride.getPickupLocationAddress()));
            edt_drop_location.setText(String.valueOf(ride.getDropLocationAddress()));
            row_apx_fare.setText(String.valueOf(ride.getTotalPrice()));
            txt_track_driver.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if(ride.getProfileImage()!=null && ride.getName() !=null){
                      showPopUp(String.valueOf(ride.getProfileImage().getImageUrl()),
                              String.valueOf(ride.getName()),
                              String.valueOf(ride.getTotalPrice()),
                              String.valueOf(ride.getDriverId()),
                              String.valueOf(ride.getRide_Id()));
                      }else {
                          showPopUp(String.valueOf(""),
                                  String.valueOf("Driver Name"),
                                  String.valueOf(ride.getTotalPrice()),
                                  String.valueOf(ride.getDriverId()),
                                  String.valueOf(""));
                      }
                  }
              });



            holder.llRootLayout.addView(view);

        }

    }


    private void showPopUp(String avatar, String driver_name, String apx_fare, final String driver_id, final String ride_id) {
        final Dialog openDialog = new Dialog(context);
        openDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.rate_driver_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RoundedImageView img_driver = openDialog.findViewById(R.id.img_driver);
        TextView txt_driver_name = openDialog.findViewById(R.id.txt_driver_name);
        TextView txt_apx_fare = openDialog.findViewById(R.id.txt_apx_fare);
        final RatingBar ratingBar1 = openDialog.findViewById(R.id.ratingBar1);
        final EditText edt_description = openDialog.findViewById(R.id.edt_description);
        Button btnAgree = openDialog.findViewById(R.id.btn_agree);


        //--------------Avatar
            Ion.with(img_driver)
                    .placeholder(R.drawable.driver)
                    .error(R.drawable.driver)
                    .load(ConstantValues.BASE_URL +"/"+avatar);

        txt_driver_name.setText(driver_name);
        txt_apx_fare.setText("$"+apx_fare);
        //------------------------

        btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String rating_bar= String.valueOf(ratingBar1.getRating());
                String _description=edt_description.getText().toString();
                prepareUnVerifiedUser(ride_id,driver_id,rating_bar,_description);
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }
//--------------------------------------rating.................
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





    public void  prepareUnVerifiedUser(final String ride_id,
                                       final String driver_id,
                                       final String rating,
                                       final String comment) {

        String createUserUrl= WebAPIManager.getInstance().get_url_rateDriver;
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty("ride_id", ride_id);
        requestBody.addProperty("driver_id", driver_id);
        requestBody.addProperty("rating", rating);
        requestBody.addProperty("comment", comment);
        createUnVerifieduser(requestBody,createUserUrl);
    }

    private void createUnVerifieduser(JsonObject requestBody, String createUserUrl) {

        Ion.with(context)
                .load(createUserUrl).addHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {
                        if(e!=null){
                            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value=String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            message = jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status){
                            case 422:

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 400:

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 201:

                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
