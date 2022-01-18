package com.example.earthquakeapplication.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.parser.DOMParser
import com.example.earthquakeapplication.parser.StreamType
import com.example.earthquakeapplication.preference.PreferenceActivity
import kotlinx.coroutines.delay
import org.xml.sax.SAXException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.xml.parsers.ParserConfigurationException

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthquakeUpdateWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            showNotification()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val outputData = Data.Builder()

        return try {
            delay(5000)
            updateEarthquakeList()

            scheduleNextUpdate(tags)

            outputData.put("Result", "Success")
            Result.Success(outputData.build())
        } catch (e: MalformedURLException) {
            outputData.put("Result", "$e")
            Result.failure()
        } catch (e: IOException) {
            outputData.put("Result", "$e")
            Result.retry()
        } catch (e: ParserConfigurationException) {
            outputData.put("Result", "$e")
            Result.failure()
        } catch (e: SAXException) {
            outputData.put("Result", "$e")
            Result.failure()
        } finally {
            hideNotification()
        }
    }

    private fun updateEarthquakeList() {
        val parser = DOMParser()
        val earthQuakes = arrayListOf<EarthQuake>()
        val quakeFeed = when (parser.streamType) {
            StreamType.Json -> {
                // JSON
                "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.geojson"
            }
            StreamType.Xml -> {
                // XML
                "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom"
            }
        }
        val url = URL(quakeFeed)

        val connection = url.openConnection() as HttpURLConnection
        when (connection.responseCode) {
            HttpURLConnection.HTTP_OK -> {
                earthQuakes.addAll(parser.parse(connection.inputStream))
            }
        }

        Database.getInstance(applicationContext)
            .earthquakeDAO
            .insertAll(earthQuakes)
    }

    private fun scheduleNextUpdate(tags: MutableSet<String>) {
        if (tags.contains(UPDATE_JOB_TAG)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val updateFreq =
                preferences.getString(PreferenceActivity.PREF_UPDATE_FREQ, "60")!!.toLong()
            val autoUpdateChecked =
                preferences.getBoolean(PreferenceActivity.PREF_AUTO_UPDATE, false)

            if (autoUpdateChecked) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val request =
                    PeriodicWorkRequestBuilder<EarthquakeUpdateWorker>(updateFreq, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .setInitialDelay(updateFreq, TimeUnit.MINUTES)
                        .addTag(PERIODIC_JOB_TAG)
                        .build()

                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
                WorkManager.getInstance(applicationContext)
                    .enqueueUniquePeriodicWork(
                        PERIODIC_JOB_TAG, ExistingPeriodicWorkPolicy.REPLACE, request
                    )
            }
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val title = "Updating"
        val description = "Updating earthquake list from server..."

        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(title)
        builder.setContentText(description)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setOngoing(true)
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun hideNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(NOTIFICATION_ID)
    }

    companion object {
        fun scheduleUpdateJob(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<EarthquakeUpdateWorker>()
                .setConstraints(constraints)
                .addTag(UPDATE_JOB_TAG)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }

        private const val TAG = "EarthquakeUpdateJobService"
        private const val UPDATE_JOB_TAG =
            "com.example.earthquakeapplication.service.EarthquakeUpdateWorker.update_job"
        private const val PERIODIC_JOB_TAG =
            "com.example.earthquakeapplication.service.EarthquakeUpdateWorker.periodic_job"

        private const val NOTIFICATION_CHANNEL_ID = "Workers"
        private const val NOTIFICATION_CHANNEL_NAME = "Update worker"
        private const val NOTIFICATION_ID = 123
    }
}