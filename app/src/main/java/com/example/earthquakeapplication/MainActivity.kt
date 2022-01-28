package com.example.earthquakeapplication

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.earthquakeapplication.databinding.ActivityMainBinding
import com.example.earthquakeapplication.earthquake.EarthquakeListFragment
import com.example.earthquakeapplication.preference.PreferenceActivity
import com.example.earthquakeapplication.search_result.SearchResultsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listFragment: EarthquakeListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            listFragment = EarthquakeListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, listFragment, TAG_LIST_FRAGMENT)
                .commit()
        } else {
            listFragment =
                supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT) as EarthquakeListFragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchableInfo =
            searchManager.getSearchableInfo(ComponentName(this, SearchResultsActivity::class.java))

        val sView = menu?.findItem(R.id.search_view)?.actionView as SearchView
        sView.setSearchableInfo(searchableInfo)
        sView.isIconifiedByDefault = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.settings_menu_item -> {
                val intent = Intent(this, PreferenceActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    @Suppress("unused")
    private fun sensorManagerTest() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        val eventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                TODO("Not yet implemented")
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                TODO("Not yet implemented")
            }
        }
        sensorManager.registerListener(
            eventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            10_000_000
        )
    }

    companion object {
        private const val TAG_LIST_FRAGMENT = "tag_list_fragment"

        const val BACKGROUND_UPLOAD_JOB_ID = 123
    }
}