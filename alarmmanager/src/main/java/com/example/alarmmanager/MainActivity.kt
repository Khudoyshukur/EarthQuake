package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import com.example.alarmmanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {

            alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, 0)
            }

            // Set the alarm to start at 8:30 a.m.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 16)
                set(Calendar.MINUTE, 57)
            }

            // setRepeating() lets you specify a precise custom interval--in this case,
            // 20 minutes.
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                1000 * 60 * 20,
                alarmIntent
            )
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TTTT", "$intent")
    }
}