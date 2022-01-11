package com.example.earthquakeapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.earthquakeapplication.databinding.ActivityMainBinding
import com.example.earthquakeapplication.fragments.EarthquakeListFragment
import com.example.earthquakeapplication.model.EarthQuake
import java.util.*

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

    override fun onResumeFragments() {
        super.onResumeFragments()

        val now = Calendar.getInstance().time
        val earthquakes = arrayListOf(
            EarthQuake("0", now, "San Jose", null, 7.3, null),
            EarthQuake("1", now, "Laa", null, 6.5, null),
        )

        listFragment.setEarthquakes(earthquakes)
    }

    companion object {
        private const val TAG_LIST_FRAGMENT = "tag_list_fragment"
    }
}