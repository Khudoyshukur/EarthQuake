package com.example.earthquakeapplication.earthquake

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.worker.EarthquakeUpdateWorker

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthQuakeListViewModel(
    private val app: Application
) : ViewModel() {
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
        loadEarthQuakes()
    }

    fun loadEarthQuakes() {
        EarthquakeUpdateWorker.scheduleUpdateJob(app.applicationContext)
    }
}