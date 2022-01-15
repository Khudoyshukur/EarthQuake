package com.example.earthquakeapplication.earthquake

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.earthquakeapplication.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeapplication.fragments.BaseFragment
import com.example.earthquakeapplication.model.EarthQuake
import com.example.earthquakeapplication.parser.DOMParser
import com.example.earthquakeapplication.parser.XmlPullParser
import com.example.earthquakeapplication.preference.PreferenceActivity

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Suppress("Deprecation")
class EarthquakeListFragment :
    BaseFragment<FragmentEarthquakeListBinding>(FragmentEarthquakeListBinding::inflate),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var listAdapter: EarthQuakeListAdapter? = null
    private lateinit var viewModel: EarthQuakeListViewModel

    private var minMagnitude = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[EarthQuakeListViewModel::class.java]

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() {
        listAdapter = EarthQuakeListAdapter()
        binding.list.adapter = listAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadEarthQuakes(DOMParser())
        }
    }

    private fun subscribeToLiveData() {
        viewModel.earthQuakes.observe(viewLifecycleOwner) {
            setEarthquakes(it)
        }
    }

    private fun setEarthquakes(earthquakes: List<EarthQuake>) {
        updateFromPreferences()

        listAdapter?.submitList(earthquakes.filter { it.mMagnitude >= minMagnitude }) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateFromPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        minMagnitude = prefs.getString(PreferenceActivity.PREF_MIN_MAG, "3")!!.toInt()
    }

    override fun onDestroyView() {
        listAdapter = null
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setEarthquakes(viewModel.earthQuakes.value ?: emptyList())
    }
}