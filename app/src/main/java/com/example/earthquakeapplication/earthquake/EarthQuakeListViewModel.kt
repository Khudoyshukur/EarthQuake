package com.example.earthquakeapplication.earthquake

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.parser.DOMParser
import com.example.earthquakeapplication.parser.EarthquakeParser
import com.example.earthquakeapplication.parser.StreamType
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthQuakeListViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    private var _earthQuakes: LiveData<List<EarthQuake>>? = null
    val earthQuakes: LiveData<List<EarthQuake>>
        get() {
            if (_earthQuakes == null) {
                _earthQuakes =
                    Database.getInstance(app.applicationContext).earthquakeDAO.getALlEarthquakes()
            }

            return _earthQuakes!!
        }

    init {
        loadEarthQuakes(DOMParser())
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("Deprecation")
    fun loadEarthQuakes(parser: EarthquakeParser) {
        object : AsyncTask<Void, Void, List<EarthQuake>>() {
            override fun doInBackground(vararg params: Void?): List<EarthQuake> {
                val earthQuakes = arrayListOf<EarthQuake>()
                try {
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
                } catch (e: Exception) {
                    Log.i(
                        "EarthQuakeListViewModel",
                        "An error occurred while loading earthquake list $e"
                    )
                }

                Database.getInstance(app.applicationContext)
                    .earthquakeDAO
                    .insertAll(earthQuakes)

                return earthQuakes
            }

            override fun onPostExecute(result: List<EarthQuake>?) {}
        }.execute()
    }
}