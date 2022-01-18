package com.example.earthquakeapplication.service

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.parser.DOMParser
import com.example.earthquakeapplication.parser.StreamType
import com.example.earthquakeapplication.preference.PreferenceActivity
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
        return try {
            updateEarthquakeList()

            scheduleNextUpdate(tags)
            Result.Success(Data.EMPTY)
        } catch (e: MalformedURLException) {
            Result.failure()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: ParserConfigurationException) {
            Result.failure()
        } catch (e: SAXException) {
            Result.failure()
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

    private suspend fun scheduleNextUpdate(tags: MutableSet<String>) {
        if (tags.contains(UPDATE_JOB_TAG)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val updateFreq =
                preferences.getString(PreferenceActivity.PREF_UPDATE_FREQ, "60")!!.toLong()
            val autoUpdateChecked =
                preferences.getBoolean(PreferenceActivity.PREF_AUTO_UPDATE, false)

            if (autoUpdateChecked) {
                WorkManager.getInstance(applicationContext)
                    .cancelAllWorkByTag(PERIODIC_JOB_TAG)
                    .await()

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                Log.i("TTTT", "update frequency $updateFreq")
                val request =
                    PeriodicWorkRequestBuilder<EarthquakeUpdateWorker>(updateFreq, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag(PERIODIC_JOB_TAG)
                        .build()

                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
                WorkManager.getInstance(applicationContext)
                    .enqueue(request)
            }
        }
    }

    companion object {
        fun scheduleUpdateJob(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<EarthquakeUpdateWorker>()
                .setConstraints(constraints)
                .addTag(UPDATE_JOB_TAG)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }

        private const val TAG = "EarthquakeUpdateJobService"
        private const val UPDATE_JOB_TAG =
            "com.example.earthquakeapplication.service.EarthquakeUpdateWorker.update_job"
        private const val PERIODIC_JOB_TAG =
            "com.example.earthquakeapplication.service.EarthquakeUpdateWorker.periodic_job"
    }
}