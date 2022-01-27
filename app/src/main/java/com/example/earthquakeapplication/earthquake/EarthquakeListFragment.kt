package com.example.earthquakeapplication.earthquake

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.earthquakeapplication.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeapplication.fragments.BaseFragment
import com.example.earthquakeapplication.model.EarthQuake
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

    private var fullScreen = false

    private var minMagnitude = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.registerOnSharedPreferenceChangeListener(this)

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enableFullscreen()
        } else {
            disableFullscreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = EarthQuakeViewModelFactory(requireActivity().application)
        viewModel = ViewModelProviders.of(this, factory)[EarthQuakeListViewModel::class.java]

        initUI()
        subscribeToLiveData()

        binding.changeFullscreen.setOnClickListener {
            if (fullScreen) {
                disableFullscreen()
            } else {
                enableFullscreen()
            }

            fullScreen = !fullScreen
        }
    }

    private fun enableFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun disableFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun initUI() {
        listAdapter = EarthQuakeListAdapter()
        binding.list.adapter = listAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadEarthQuakes()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.earthQuakes.observe(viewLifecycleOwner) {
            Log.i("TTTT", "$it")
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