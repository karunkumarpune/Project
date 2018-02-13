package com.app.bickup_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private SharedPreferences Image_Sp;
    private SharedPreferences Image_Sp1;

    private SharedPreferences pref_pickup;
    private SharedPreferences pref_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_setting);
        initializeViews();

        Image_Sp = this.getSharedPreferences("Image_Sp", 0);
        Image_Sp1 = this.getSharedPreferences("LoginInfos", 0);
    }
    private void initializeViews() {
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_setting));
        findViewById(R.id.btn_logout).setOnClickListener(this);
        imgBack= findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s=new Intent(SettingActivity.this,EditProfileActivity.class);
                startActivity(s);
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                finish();
                break;
            case R.id.btn_logout:

                pref_pickup = getSharedPreferences("MyPickup", Context.MODE_PRIVATE);
                pref_drop = getSharedPreferences("MyDrop", Context.MODE_PRIVATE);
                SharedPreferences.Editor pic_edit = pref_pickup.edit();
                pic_edit.clear();
                pic_edit.apply();

                SharedPreferences.Editor drop_edit = pref_drop.edit();
                drop_edit.clear();
                drop_edit.apply();


                 SharedPreferences.Editor editor=Image_Sp.edit();
                editor.clear();
                editor.apply();

                SharedPreferences.Editor editor1=Image_Sp1.edit();
                editor1.clear();
                editor1.apply();

                GloableVariable.Tag_drop_location_check="";
                GloableVariable.Tag_pickup_location_address="";
                 GloableVariable.Tag_pickup_latitude=0.0;
                GloableVariable.Tag_pickup_longitude=0.0;

                GloableVariable.Tag_pickup_home_type="";
                GloableVariable.Tag_pickup_villa_no="";
                GloableVariable.Tag_pickup_building_name="";
                GloableVariable.Tag_pickup_floor_number="";
                GloableVariable.Tag_pickup_unit_number="";


                GloableVariable.Tag_pickup_Contact_type="";
                GloableVariable.Tag_pickup_contact_name="";
                GloableVariable.Tag_pickup_contact_number="";
                GloableVariable.Tag_pickup_comments="";



                GloableVariable.Tag_drop_location_address="";
                GloableVariable.Tag_drop_latitude=0.0;
                GloableVariable.Tag_drop_longitude=0.0;


                GloableVariable.Tag_drop_home_type="";
                GloableVariable.Tag_drop_villa_no="";
                GloableVariable.Tag_drop_building_name="";
                GloableVariable.Tag_drop_floor_number="";
                GloableVariable.Tag_drop_unit_number="";



                GloableVariable.Tag_drop_Contact_type="";
                GloableVariable.Tag_drop_contact_name="";
                GloableVariable.Tag_drop_contact_number="";
                GloableVariable.Tag_drop_comments="";




                GloableVariable.Tag_helper="";
                GloableVariable.Tag_Good_Details_Description="";

                GloableVariable.Tag_Good_Details_Comming_time_type="";
                GloableVariable.Tag_Good_Details_Comming_Date_time="";

                GloableVariable.Tag_type_of_goods="";

                 GloableVariable.Tag_distance="";
                 GloableVariable.Tag_total_price="";
                 GloableVariable.Tag_total_final_prices="";
                 GloableVariable.Tag_booking_id="";

                 GloableVariable.Tag_paid_by_type="";
                 GloableVariable.Tag_paid_by_name="";
                 GloableVariable.paid_by_contact_number="";




                CommonMethods.getInstance().clearSharePreferences(this, ConstantValues.USER_PREFERENCES);
                Intent setting=new Intent(this,LoginActivity.class);
                startActivity(setting);
                finishAffinity();
                break;
        }
    }
}
