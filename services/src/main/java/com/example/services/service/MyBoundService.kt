package com.example.services.service

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class MyBoundService : Service() {

    private val binder = MyBinder()

    private var task: Task? = null

    fun getRandomInt(): Int {
        return (1..12222).random()
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        task = Task()
        task?.execute()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        log("onBind")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        log("onRebind")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")

        task?.cancel(true)
        task = null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        log("onUnbind")
        return false
    }

    inner class MyBinder : Binder() {
        val service get() = this@MyBoundService
    }

    private fun log(message: String) {
        Log.i("TTTT", message)
    }

    private inner class Task : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void {
            while (true) {
                log("Service is running")
                Thread.sleep(1000)
            }
        }
    }
}