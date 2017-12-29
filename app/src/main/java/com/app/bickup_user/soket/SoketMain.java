package com.app.bickup_user.soket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.bickup_user.utility.ConstantValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SoketMain extends AppCompatActivity {

    private Context context;
    private Socket socket;
    private String TAG="SoketMain";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        connectSocketForLocationUpdates();
    }
    private void connectSocketForLocationUpdates() {
        try {
            socket = IO.socket(ConstantValues.BASE_URL);
            socket.connect();
            Log.d(TAG, "Socket ID : " + socket.id());


            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(final Object... args) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[1];
                            String message;
                            try {
                                message = data.getString("trackDriverLocation").toString();
                                Log.d(TAG, "message: " + message);
                            } catch (JSONException e) {
                            }


                        }
                    });
                }


            }).on("event", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }
            });






        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

}
