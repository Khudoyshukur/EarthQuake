package com.example.earthquakeapplication.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.earthquakeapplication.R

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_preferences, null)
    }
}