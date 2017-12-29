package com.app.bickup_user.select_driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.bickup_user.R;
import java.util.ArrayList;
import java.util.HashMap;


public class DriverActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Driver_Model> list;
    private RecyclerView recyclerView;
    private DriverAdapter myAdapter;
    private String data;
    private TextView txt_activty_header;
    private ImageView backImage_header, img_tick_toolbar;
    private SearchView search_view;
    private ArrayList<HashMap<String, String>> mylist;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_goods);
        list = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.list_types_goods);
        search_view = (SearchView) findViewById(R.id.search_view);
        backImage_header = (ImageView) findViewById(R.id.backImage_header);
        img_tick_toolbar = (ImageView) findViewById(R.id.img_tick_toolbar);
        txt_activty_header = (TextView) findViewById(R.id.txt_activty_header);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        txt_activty_header.setText(getResources().getString(R.string.txt_contact_driver));
        backImage_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_tick_toolbar.setVisibility(View.VISIBLE);
        img_tick_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // data = getIntent().getStringExtra("key");

        list.add(new Driver_Model("MC545646","karun","https://www.rideapps.co/wp-content/uploads/2015/11/driver-uber-750x500.jpg"));
        list.add(new Driver_Model("TT545646","RAMES","https://daymondjohnssuccessformula.com/wp-content/uploads/sites/23/2016/09/5-ways-to-become-a-better-uber-driver-2-862x575.jpg?x76415"));
        list.add(new Driver_Model("KR545646","SHIOW","https://www.mynrma.com.au/-/media/safer-driving-school/learner-driver-mobile.jpg?h=850&la=en&w=828&hash=7600DE4A51B87061397F059B698D5240067F3C96"));
  list.add(new Driver_Model("MC545646","karun","https://www.rideapps.co/wp-content/uploads/2015/11/driver-uber-750x500.jpg"));
        list.add(new Driver_Model("TT545646","RAMES","https://daymondjohnssuccessformula.com/wp-content/uploads/sites/23/2016/09/5-ways-to-become-a-better-uber-driver-2-862x575.jpg?x76415"));
        list.add(new Driver_Model("KR545646","SHIOW","https://www.mynrma.com.au/-/media/safer-driving-school/learner-driver-mobile.jpg?h=850&la=en&w=828&hash=7600DE4A51B87061397F059B698D5240067F3C96"));
  list.add(new Driver_Model("MC545646","karun","https://www.rideapps.co/wp-content/uploads/2015/11/driver-uber-750x500.jpg"));
        list.add(new Driver_Model("TT545646","RAMES","https://daymondjohnssuccessformula.com/wp-content/uploads/sites/23/2016/09/5-ways-to-become-a-better-uber-driver-2-862x575.jpg?x76415"));
        list.add(new Driver_Model("KR545646","SHIOW","https://www.mynrma.com.au/-/media/safer-driving-school/learner-driver-mobile.jpg?h=850&la=en&w=828&hash=7600DE4A51B87061397F059B698D5240067F3C96"));
  list.add(new Driver_Model("MC545646","karun","https://www.rideapps.co/wp-content/uploads/2015/11/driver-uber-750x500.jpg"));
        list.add(new Driver_Model("TT545646","RAMES","https://daymondjohnssuccessformula.com/wp-content/uploads/sites/23/2016/09/5-ways-to-become-a-better-uber-driver-2-862x575.jpg?x76415"));
        list.add(new Driver_Model("KR545646","SHIOW","https://www.mynrma.com.au/-/media/safer-driving-school/learner-driver-mobile.jpg?h=850&la=en&w=828&hash=7600DE4A51B87061397F059B698D5240067F3C96"));


        myAdapter = new DriverAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        setupSearchView();

    }

    private void setupSearchView() {
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.isSubmitButtonEnabled();
        search_view.setQueryHint("Search Here");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        myAdapter.filter(newText);
        return true;
    }
}