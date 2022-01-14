package com.example.sqllite.app

import android.app.Application
import com.example.sqllite.database.HoardDatabase

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        HoardDatabase.initialize(this)
    }
}