package com.app.bickup_user.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.bickup_user.R
import kotlinx.android.synthetic.main.activity_service.*


class ServiceActivity : AppCompatActivity() {
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_service)
      
      
      btn_1.setOnClickListener {
         val ishintent = Intent(this, ServicesActivity::class.java)
         val pintent = PendingIntent.getService(this, 0, ishintent, 0)
         val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
         alarm.cancel(pintent)
         alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pintent)
         
         
      }
      
      btn_2.setOnClickListener {
         stopService(Intent(this, MyService::class.java))
   
      }
   }
}
