package com.app.bickup_user.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
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
import com.koushikdutta.ion.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SchedulAdapter extends RecyclerView.Adapter<SchedulAdapter.MyViewHolder> {
    private APIService mAPIService;
    private Context context;
    private LayoutInflater inflater;
    private List<Responses> list;
    private String message;



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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
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




              txt_recshedul.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      holder.showPopUp(ride.getRide_Id());
                  }
              });

              holder.Ride_Id=ride.getRide_Id();
              txt_cancel_schedule.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      holder.prepareCancel_schedule(ride.getRide_Id());
                  }
              });




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
        private Typeface mTypefaceRegular;
        private Typeface mTypefaceBold;
        private String add_date_time;
        private String add_mit="";
        private String add_hours="";
        private String Ride_Id;


        public MyViewHolder(View view) {
            super(view);
            today_txt= view.findViewById(R.id.tvDate);
            tvNoOfDeliveries= view.findViewById(R.id.tvNoOfDeliveries);
            llRootLayout = view.findViewById(R.id.llRootLayout);

        }

        //Data Pasese----------------------------------

        public void  prepareCancel_schedule(final String ride_id) {
            final JsonObject requestBody=new JsonObject();
            requestBody.addProperty("ride_id", ride_id);
            createUnVerifieduser(requestBody,WebAPIManager.getInstance().get_url_Cancel);
        }

        public void  prepareRecshedul(final String ride_id,final String reschedule_timestamp) {
            final JsonObject requestBody=new JsonObject();
            requestBody.addProperty("ride_id", ride_id);
            requestBody.addProperty("reschedule_timestamp", reschedule_timestamp);
            createUnVerifieduser(requestBody,WebAPIManager.getInstance().get_url_Reschedule);
        }
        private void createUnVerifieduser(JsonObject requestBody, String createUserUrl) {

            Ion.with(context)
                    .load(createUserUrl).addHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setJsonObjectBody(requestBody)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
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




        //............Parse End




        //--------------------------Calender-------------------------------
        private void showPopUp(String Ride_Id) {
            final Dialog openDialog = new Dialog(context);
            openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            openDialog.setContentView(R.layout.calender_view_dialog);
            openDialog.setTitle("Custom Dialog Box");
            openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button btnCancel = (Button) openDialog.findViewById(R.id.btn_cancel);
            final CustomCalendarView calendarView = (CustomCalendarView) openDialog.findViewById(R.id.calendar_view);
            final Button btnok = (Button) openDialog.findViewById(R.id.btn_ok);


            //Initialize calendar with date
            Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
            add_date_time= new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            calendarView.setFirstDayOfWeek(Calendar.MONDAY);
            calendarView.setShowOverflowDate(false);
            calendarView.refreshCalendar(currentCalendar);
            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date) {

                    if (!CalendarUtils.isPastDay(date)) {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String dates=df.format(date);
                        add_date_time=dates;
                        btnok.setEnabled(true);
                    } else {
                        btnok.setEnabled(false);
                        Toast.makeText(getApplicationContext(),"Please select valid date!",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onMonthChanged(Date date) {
                    //  SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                    //  Toast.makeText(getApplicationContext(), df.format(date), Toast.LENGTH_SHORT).show();
                }
            });


            //adding calendar day decorators
            List<DayDecorator> decorators = new ArrayList<>();
            decorators.add(new DisabledColorDecorator());
            calendarView.setDecorators(decorators);
            calendarView.refreshCalendar(currentCalendar);
            btnCancel.setTypeface(mTypefaceRegular);
            btnok.setTypeface(mTypefaceRegular);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                }
            });
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    showPopUpForTime();
                }
            });
            openDialog.show();
        }


        private void showPopUpForTime() {
            final Dialog openDialog = new Dialog(context);
            openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            openDialog.setContentView(R.layout.time_dialog);
            openDialog.setTitle("Custom Dialog Box");
            openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            final TextView fifteenMinuit = openDialog.findViewById(R.id.fifteen_minuit);
            final TextView thirtyMinuit = openDialog.findViewById(R.id.thirty_minuit);
            final TextView fourtyMinuit = openDialog.findViewById(R.id.fourty_minuit);
            final TextView minuitText = openDialog.findViewById(R.id.minuit_txt);
            final TextView hourtext = openDialog.findViewById(R.id.hourtext);
            minuitText.setTypeface(mTypefaceRegular);
            fifteenMinuit.setTypeface(mTypefaceRegular);
            thirtyMinuit.setTypeface(mTypefaceRegular);
            fourtyMinuit.setTypeface(mTypefaceRegular);
            setColorWhite(fifteenMinuit);
            setColorWhite(thirtyMinuit);
            setColorWhite(fourtyMinuit);
            final com.xw.repo.BubbleSeekBar seekBar = openDialog.findViewById(R.id.seekbar);
            add_hours="1";
            seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {
                    hourtext.setText(String.valueOf(progress));
                    add_hours=String.valueOf(progress);
                }
                @Override
                public void getProgressOnActionUp(int progress, float progressFloat) {

                }
                @Override
                public void getProgressOnFinally(int progress, float progressFloat) {
                }
            });

            add_mit="15";
            fifteenMinuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minuitText.setText("15");
                    add_mit="15";
                    fifteenMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_text_color));
                    thirtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    fourtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    setColorYellow(view);
                    setColorWhite(thirtyMinuit);
                    setColorWhite(fourtyMinuit);
                }
            });

            thirtyMinuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minuitText.setText("30");
                    add_mit="30";
                    thirtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_text_color));
                    fifteenMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    fourtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    setColorYellow(view);
                    setColorWhite(fifteenMinuit);
                    setColorWhite(fourtyMinuit);
                }
            });

            fourtyMinuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColorYellow(view);
                    minuitText.setText("45");
                    add_mit="45";
                    fourtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_text_color));
                    fifteenMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    thirtyMinuit.setTextColor(getApplicationContext().getResources().getColor(R.color.greyColor));
                    setColorWhite(thirtyMinuit);
                    setColorWhite(fifteenMinuit);
                }
            });

            Button btnCancel = openDialog.findViewById(R.id.btn_cancel);
            Button btnok = openDialog.findViewById(R.id.btn_ok);
            btnCancel.setTypeface(mTypefaceRegular);
            btnok.setTypeface(mTypefaceRegular);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                }
            });
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    Log.d("TAGS Time",add_date_time +"    "+add_hours+":"+ add_mit);
                    GloableVariable.Tag_Good_Details_Comming_Date_time=add_date_time +"  "+add_hours+":"+ add_mit;
                    String[] parsedate= add_date_time.split("/");
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(parsedate[2]),
                            Integer.parseInt(parsedate[1]) - 1,
                            Integer.parseInt(parsedate[0]),
                            Integer.parseInt(add_hours),
                            Integer.parseInt(add_mit));
                    long timestamp = c.getTimeInMillis();
                    prepareRecshedul(Ride_Id, String.valueOf(timestamp));
                }
            });
            openDialog.show();
        }

        private void setColorYellow(View view) {
            TextView view1 = (TextView) view;
            StateListDrawable bgShape = (StateListDrawable) view1.getBackground();
            bgShape.setColorFilter(Color.parseColor("#e6ba13"), PorterDuff.Mode.SRC_ATOP);
        }

        private void setColorWhite(TextView view) {
            StateListDrawable bgShape = (StateListDrawable) view.getBackground().mutate();
            bgShape.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        }


        private class DisabledColorDecorator implements DayDecorator {
            @Override
            public void decorate(DayView dayView) {
                if (CalendarUtils.isPastDay(dayView.getDate())) {
                    int color = Color.parseColor("#a9afb9");
                    dayView.setBackgroundColor(color);
                }
            }
        }



    }

}
