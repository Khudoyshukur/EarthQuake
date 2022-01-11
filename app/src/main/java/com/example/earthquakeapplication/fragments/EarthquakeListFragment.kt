package com.example.earthquakeapplication.fragments

import android.os.Bundle
import android.view.View
import com.example.earthquakeapplication.adapters.EarthQuakeListAdapter
import com.example.earthquakeapplication.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeapplication.model.EarthQuake

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthquakeListFragment :
    BaseFragment<FragmentEarthquakeListBinding>(FragmentEarthquakeListBinding::inflate) {

    private var listAdapter: EarthQuakeListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        listAdapter = EarthQuakeListAdapter()
        binding.root.adapter = listAdapter
    }

    fun setEarthquakes(earthquakes: List<EarthQuake>) {
        listAdapter?.submitList(earthquakes)
    }

    override fun onDestroyView() {
        listAdapter = null
        super.onDestroyView()
    }
}