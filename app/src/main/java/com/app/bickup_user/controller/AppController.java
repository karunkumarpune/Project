package com.app.bickup_user.controller;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.app.bickup_user.broadcastreciever.InternetConnectionBroadcast;

/**
 * Created by fluper-pc on 14/9/17.
 */

    public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController minstance;




    @Override
    public void onCreate() {
        super.onCreate();
        minstance=AppController.this;
    }

    public static synchronized AppController getInstance(){
        return minstance;
    }

    public void setConnectivityListener(InternetConnectionBroadcast.ConnectivityRecieverListener listener) {
        InternetConnectionBroadcast.mConnectivityRecieverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
