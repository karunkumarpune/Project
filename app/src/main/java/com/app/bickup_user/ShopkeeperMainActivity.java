package com.app.bickup_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.fragments.OngoingPickupFragment;
import com.app.bickup_user.fragments.PointsFragments;
import com.app.bickup_user.model.User;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;

public class ShopkeeperMainActivity extends AppCompatActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_main);

        ImageView navigaionImage=(ImageView)findViewById(R.id.backImage_header);
        final DuoDrawerLayout drawerLayout =(DuoDrawerLayout)findViewById(R.id.drawer_layout);

        findViewById(R.id.menu_delivery).setOnClickListener(this);
        findViewById(R.id.menu_scheduled).setOnClickListener(this);
        findViewById(R.id.menu_setting).setOnClickListener(this);
        findViewById(R.id.img_drawer_image).setOnClickListener(this);
        TextView userName=(TextView) findViewById(R.id.username);
        TextView useremail=(TextView) findViewById(R.id.userEmail);

        userName.setText(User.getInstance().getFirstName()+" "+User.getInstance().getLastName());
        useremail.setText(User.getInstance().getEmail());


        navigaionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        ImageView rightheader=(ImageView)findViewById(R.id.img_tick_toolbar);
        TextView heade=(TextView)findViewById(R.id.txt_activty_header);

        heade.setText(getResources().getString(R.string.app_name));

        navigaionImage.setImageResource(R.drawable.sidebar);
        rightheader.setImageResource(R.drawable.driver2);
        rightheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShopkeeperMainActivity.this,TypesGoods.class);
                intent.putExtra(TrackDriverActivity.OPENTYPESGOODS,1);
                startActivity(intent);
            }
        });
        rightheader.setVisibility(View.VISIBLE);
        navigaionImage.setVisibility(View.VISIBLE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.menu_delivery:
                Intent intent = new Intent(this, DeliveryActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_scheduled:
                Intent scheDuled = new Intent(this, ScheduledActivity.class);
                startActivity(scheDuled);
                break;
            case R.id.menu_setting:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                break;

        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           if(position==0){
             return new PointsFragments();
           }else {
            return new OngoingPickupFragment();
           }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.txt_points);
                case 1:
                    return getResources().getString(R.string.txt_ongoing_pickup);

            }
            return null;
        }
    }
}
