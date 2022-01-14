package com.example.earthquakeapplication.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.earthquakeapplication.R
import com.example.earthquakeapplication.databinding.ActivityPreferenceBinding

class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, PreferenceFragment::class.java, null)
                .commit()
    }

    companion object {
        const val PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE"
        const val USER_PREFERENCE = "USER_PREFERENCE"
        const val PREF_MIN_MAG = "PREF_MIN_MAG"
        const val PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ"
    }
}