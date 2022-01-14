package com.example.downloadmanager.presentation

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class StatusReceiver(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val onReceived: (Intent?) -> Unit
) : DefaultLifecycleObserver {

    private var receiver: Receiver? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        receiver = Receiver()
        val filter = IntentFilter()
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(receiver!!, filter)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        context.unregisterReceiver(receiver!!)
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                onReceived.invoke(intent)
            }
        }
    }
}