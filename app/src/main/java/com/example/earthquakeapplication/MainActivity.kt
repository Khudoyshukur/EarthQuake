package com.example.earthquakeapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.earthquakeapplication.databinding.ActivityMainBinding
import com.example.earthquakeapplication.earthquake.EarthquakeListFragment
import com.example.earthquakeapplication.preference.PreferenceActivity

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
        super.onCreateOptionsMenu(menu)
        menu?.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_settings)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            MENU_PREFERENCES -> {
                val intent = Intent(this, PreferenceActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    companion object {
        private const val TAG_LIST_FRAGMENT = "tag_list_fragment"
        private const val MENU_PREFERENCES = Menu.FIRST + 1
    }
}