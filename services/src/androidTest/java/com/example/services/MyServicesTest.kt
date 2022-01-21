package com.example.services

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.example.services.service.MyBoundService
import com.example.services.service.MyForegroundService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.any
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException
import kotlin.jvm.Throws

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@RunWith(AndroidJUnit4::class)
class MyServicesTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    @Test
    @Throws(TimeoutException::class)
    fun testMyBoundService() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MyBoundService::class.java
        )

        val binder = serviceRule.bindService(intent)

        val service = (binder as MyBoundService.MyBinder).service

        assertThat(service.getRandomInt(), `is`(any(Int::class.java)))
    }

    @Test
    @Throws(TimeoutException::class)
    fun testForegroundService() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MyForegroundService::class.java
        )

        serviceRule.startService(intent)
    }
}