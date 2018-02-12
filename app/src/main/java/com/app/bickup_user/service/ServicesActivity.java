package com.app.bickup_user.service;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.bickup_user.R;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
    }
}
