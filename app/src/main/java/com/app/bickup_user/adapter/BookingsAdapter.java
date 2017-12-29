package com.app.bickup_user.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickup_user.DeliveryActivity;
import com.app.bickup_user.R;

import java.util.ArrayList;

public class BookingsAdapter extends BaseAdapter {
    private ArrayList<String> goodsList;
    private DeliveryActivity activity;
    private Activity activity1;
    private String[] array;
    private LayoutInflater inflater;
    int openRow;


    public BookingsAdapter(DeliveryActivity activity, String[] array,int openRow){
        //this.goodsList=goodsList;
        this.openRow=openRow;
        this.array=array;
        this.activity=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }
    public BookingsAdapter(Activity activity, String[] array, int openRow){
        //this.goodsList=goodsList;
        this.openRow=openRow;
        this.array=array;
        this.activity1=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return (null != array ? array.length : 0);
    }

    @Override
    public Object getItem(int i) {
        return array[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView=null;
        if(openRow==0) {
            rowView = inflater.inflate(R.layout.row_history_list, null);
            RelativeLayout relativeLayout=(rowView.findViewById(R.id.rl_corner));
            if(i==1){
                TextView text1=(rowView.findViewById(R.id.row_apx_fare));
                TextView text2=(rowView.findViewById(R.id.row_label_txt_fare));
                text1.setTextColor(Color.parseColor("#d32f2e"));
                text2.setTextColor(Color.parseColor("#d32f2e"));
                relativeLayout.setVisibility(View.VISIBLE);
            }
            TextView textView=(TextView)rowView.findViewById(R.id.txt_rate_driver_now);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //  showPopUp(0);

                }
            });

        }else {
            rowView = inflater.inflate(R.layout.ongoing_deliveries, null);
            TextView textView=(TextView)rowView.findViewById(R.id.txt_rate_driver_now);
           textView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

               }
           });
            //    ImageView imageView=(ImageView)rowView.findViewById(R.id.img_row_checkbox);
            //  imageView.setTag(false);
        }

        return rowView;
    }


    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(activity);
        openDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.rate_driver_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       /* TextView travellerName = (TextView)openDialog.findViewById(R.id.txt_traveller_name_dialog);*/

       /* TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);
        Button btnDisagree = (Button)openDialog.findViewById(R.id.btn_disagree);
*/
        Button btnAgree = (Button)openDialog.findViewById(R.id.btn_agree);
       /* travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceBold);
        btnAgree.setTypeface(mTypefaceBold);*/
      /*  if(choosetraveller==1){

        }*/
     /*   btnDisagree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });*/
        btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();

            }
        });
        openDialog.show();

    }


  /*  private void flipCard() {
        activity.getFragmentManager().beginTransaction()
                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(R.anim.flip_animation, R.anim.flip_animation2)

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).
                .add(R.id.booking_container,new BlankFragment())
               .commit();
    }*/
}
