package com.app.bickup_user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickup_user.controller.AppController;
import com.app.bickup_user.fragments.HistoryFragment;
import com.app.bickup_user.fragments.OnGoingFragment;
import com.app.bickup_user.interfaces.OpenratingInterface;
import com.app.bickup_user.utility.CommonMethods;
import com.wajahatkarim3.easyflipview.EasyFlipView;

public class DeliveryActivity extends AppCompatActivity  implements View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener,OpenratingInterface {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private ImageView imgBack;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_delivery2);
       // setupWindowAnimations();
        initializeViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupWindowAnimations() {
        Slide fade = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.slide_transition);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private void initializeViews() {
        mActivityreference=this;
        //mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_content);
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_deliveries));
         imgBack=(ImageView)findViewById(R.id.backImage_header);
         imgBack.setVisibility(View.VISIBLE);
         imgBack.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                finish();
                break;
        }
    }

    @Override
    public void openDialog() {
        RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.booking_container);
        EasyFlipView easyFlipView=(EasyFlipView) findViewById(R.id.flip_view);
        relativeLayout.setVisibility(View.VISIBLE);
        easyFlipView.flipTheView();
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_delivery, container, false);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                     fragment=new OnGoingFragment();
                    break;
                case 1:
                     fragment=new HistoryFragment();
                    break;
            }
           return fragment;
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.txt_ongoing);
                case 1:
                    return getResources().getString(R.string.txt_history);

            }
            return null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        initializeViews();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    private void checkInternetconnection() {
        mIsConnected = CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if (mIsConnected) {
            if (snackbar != null) {
                snackbar.dismiss();

            }
        } else {
            //showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

   /* public void showSnackBar(String mString) {
        snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
    }*/

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            //showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
}
