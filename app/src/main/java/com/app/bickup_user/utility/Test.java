package com.app.bickup_user.utility;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.R;
import com.app.bickup_user.fragments.GoodsDetailsFragments;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;
import com.xw.repo.BubbleSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Test extends AppCompatActivity{
    public static String TAG = GoodsDetailsFragments.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private String add_date_time;
    private String add_mit="";
    private String add_hours="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.fragment_goods_details_fragments);

        showPopUp(0);
    }


    //--------------------------Calender-------------------------------
    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(this);
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
        final Dialog openDialog = new Dialog(this);
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
                GloableVariable.Tag_Good_Details_Comming_Date_time_Stamp=timestamp;
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
