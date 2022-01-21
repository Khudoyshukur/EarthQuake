package com.example.services.service

import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.os.bundleOf

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class HelloService : Service() {

    private lateinit var serviceHandler: ServiceHandler
    private lateinit var serviceLooper: Looper

    private var receiver: ResultReceiver? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            Thread.sleep(2000)
            sendMessageBroadcast("Started word")
            Thread.sleep(5000)
            sendMessageBroadcast("Finished fork")
            Thread.sleep(2000)

            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()

        HandlerThread("ServiceHandlerThread", Thread.NORM_PRIORITY).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        receiver = intent?.extras?.getParcelable("receiver") as? ResultReceiver
        sendMessageBroadcast("onStartCommand")


        serviceHandler.obtainMessage().also {

            it.arg1 = startId
            serviceHandler.sendMessage(it)
        }

        return START_REDELIVER_INTENT
    }

    private fun sendMessageBroadcast(message: String) {
        receiver?.send(111, bundleOf("extra" to message))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sendMessageBroadcast("onDestroy")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}