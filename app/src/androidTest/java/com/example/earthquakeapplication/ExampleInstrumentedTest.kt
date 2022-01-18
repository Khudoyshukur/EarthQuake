package com.example.earthquakeapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import com.example.earthquakeapplication.worker.EarthquakeUpdateWorker
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.earthquakeapplication", appContext.packageName)
    }

    @Test
    fun testPeriodicWork() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = PeriodicWorkRequestBuilder<EarthquakeUpdateWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("Test")
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork("Test", ExistingPeriodicWorkPolicy.REPLACE, request)
    }

    @Test
    fun testWorkers() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = PeriodicWorkRequestBuilder<EarthquakeUpdateWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("Test")
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork("Test", ExistingPeriodicWorkPolicy.REPLACE, request)

        val workInfo = workManager.getWorkInfoById(request.id).get()

        assert(workInfo.state == WorkInfo.State.ENQUEUED)
    }
}