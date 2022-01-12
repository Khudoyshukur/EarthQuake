package com.example.earthquakeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.earthquakeapplication.adapters.EarthQuakeListAdapter
import com.example.earthquakeapplication.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeapplication.parser.XmlPullParser
import com.example.earthquakeapplication.viewmodel.EarthQuakeListViewModel

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Suppress("Deprecation")
class EarthquakeListFragment :
    BaseFragment<FragmentEarthquakeListBinding>(FragmentEarthquakeListBinding::inflate) {

    private var listAdapter: EarthQuakeListAdapter? = null
    private lateinit var viewModel: EarthQuakeListViewModel

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
            viewModel.loadEarthQuakes(XmlPullParser())
        }
    }

    private fun subscribeToLiveData() {
        viewModel.earthQuakes.observe(viewLifecycleOwner) {
            listAdapter?.submitList(it) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        listAdapter = null
        super.onDestroyView()
    }
}