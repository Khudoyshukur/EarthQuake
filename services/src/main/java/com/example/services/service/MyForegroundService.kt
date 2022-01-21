package com.example.services.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.services.R
import kotlin.concurrent.thread

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

private const val NOTIFICATION_ID = 123
private const val NOTIFICATION_CHANNEL_ID = "Foreground service"
private const val NOTIFICATION_NAME = "MyForegroundService"

class MyForegroundService : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification =
            createNotification(
                "Foreground",
                "Doing foreground work",
                R.drawable.ic_launcher_foreground
            )

        startForeground(NOTIFICATION_ID, notification)

        doSomeWorkAndStop(startId)

        return START_STICKY
    }

    private fun doSomeWorkAndStop(startId: Int) {
        thread(start = true) {
            for (i in 0..100) {
                Thread.sleep(1000)
                println("MyForegroundService is running $i")
            }

            stopSelf(startId)
        }
    }

    private fun createNotification(title: String, content: String, smallIcon: Int): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}