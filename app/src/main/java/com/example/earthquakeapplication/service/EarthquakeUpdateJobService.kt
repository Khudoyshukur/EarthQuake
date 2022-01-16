package com.example.earthquakeapplication.service

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.parser.DOMParser
import com.example.earthquakeapplication.parser.EarthquakeParser
import com.example.earthquakeapplication.parser.StreamType
import com.example.earthquakeapplication.preference.PreferenceActivity
import com.firebase.jobdispatcher.*
import org.xml.sax.SAXException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.ParserConfigurationException

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthquakeUpdateJobService : SimpleJobService() {

    override fun onRunJob(job: JobParameters?): Int {
        val parser = DOMParser()

        val earthQuakes = arrayListOf<EarthQuake>()
        return try {
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

            scheduleNextUpdate(job)

            RESULT_SUCCESS
        } catch (e: MalformedURLException) {
            RESULT_FAIL_NORETRY
        } catch (e: IOException) {
            RESULT_FAIL_RETRY
        } catch (e: ParserConfigurationException) {
            RESULT_FAIL_NORETRY
        } catch (e: SAXException) {
            RESULT_FAIL_NORETRY
        }
    }

    private fun scheduleNextUpdate(job: JobParameters?) {
        if (job?.tag == UPDATE_JOB_TAG) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val updateFreq =
                preferences.getString(PreferenceActivity.PREF_UPDATE_FREQ, "60")!!.toInt()
            val autoUpdateChecked =
                preferences.getBoolean(PreferenceActivity.PREF_AUTO_UPDATE, false)

            if (autoUpdateChecked) {
                val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(applicationContext))

                dispatcher.schedule(
                    dispatcher.newJobBuilder()
                        .setTag(PERIODIC_JOB_TAG)
                        .setReplaceCurrent(true)
                        .setRecurring(true)
                        .setTrigger(
                            Trigger.executionWindow(
                                updateFreq * 60 / 2,
                                updateFreq * 60
                            )
                        )
                        .setService(EarthquakeUpdateJobService::class.java)
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .build()
                )
            }
        }
    }

    companion object {
        fun scheduleUpdateJob(context: Context) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

            dispatcher.schedule(
                dispatcher.newJobBuilder()
                    .setTag(UPDATE_JOB_TAG)
                    .setService(EarthquakeUpdateJobService::class.java)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build()
            )
        }

        private const val TAG = "EarthquakeUpdateJobService"
        private const val UPDATE_JOB_TAG = "update_job"
        private const val PERIODIC_JOB_TAG = "periodic_job"
    }
}