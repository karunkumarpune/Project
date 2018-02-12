package com.app.bickup_user.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class MyService(name: String) : Service() {
   
   private lateinit var player: MediaPlayer
   
   override fun onBind(p0: Intent?): IBinder? {
      
      return null;
      Log.d("TAGS", "onBind")
   
   }
   
   override fun onCreate() {
      super.onCreate()
      
      Log.d("TAGS", "onCreate")
   }
   
   override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
      player = MediaPlayer.create(this,
       Settings.System.DEFAULT_RINGTONE_URI);
   
      player.setLooping(true);
   
      //staring the player
      player.start();
      
      
      Log.d("TAGS", "onStartCommand")
   
      return START_STICKY
   }
   
   override fun onDestroy() {
      super.onDestroy()
      player.stop();
   
   
      Log.d("TAGS", "onDestroy")
   
   }
}