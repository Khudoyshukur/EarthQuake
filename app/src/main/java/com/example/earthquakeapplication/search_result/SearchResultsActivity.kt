package com.example.earthquakeapplication.search_result

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.databinding.ActivitySearchResultsBinding
import com.example.earthquakeapplication.earthquake.EarthQuakeListAdapter
import com.example.earthquakeapplication.model.EarthQuake

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultsBinding
    private lateinit var adapter: EarthQuakeListAdapter

    private lateinit var results: LiveData<List<EarthQuake>>
    private lateinit var query: MutableLiveData<String>
    private fun setQuery(query: String) {
        this.query.value = query
    }

    private lateinit var selectedSearchId: MutableLiveData<String>
    private fun setSelectedSearchId(uri: Uri) {
        val id = uri.lastPathSegment
        this.selectedSearchId.value = id
    }

    private lateinit var selectedSearchSuggestion: LiveData<EarthQuake>
    private val selectedSearchObserver = Observer<EarthQuake> {
        if (it != null) {
            setQuery(it.mDetails)
        }
    }

    private val searchResultObserver = Observer<List<EarthQuake>> {
        adapter.submitList(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)

        initUI()
    }

    private fun initUI() {
        adapter = EarthQuakeListAdapter()
        binding.root.adapter = adapter

        query = MutableLiveData(null)

        results = Transformations.switchMap(query) {
            Database.getInstance(this)
                .earthquakeDAO
                .searchEarthquakes("%$it%")
        }

        results.observe(this, searchResultObserver)

        selectedSearchId = MutableLiveData(null)
        selectedSearchSuggestion = Transformations.switchMap(selectedSearchId) {
            Database.getInstance(this)
                .earthquakeDAO
                .getEarthquake(it)
        }

        if (intent.action == Intent.ACTION_VIEW) {
            selectedSearchSuggestion.observe(this, selectedSearchObserver)
            setSelectedSearchId(intent.data ?: return)
        } else {
            val query = intent.getStringExtra(SearchManager.QUERY)
            setQuery(query ?: "")
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        intent = newIntent

        if (intent.action == Intent.ACTION_VIEW) {
            setSelectedSearchId(intent.data ?: return)
        } else {
            val query = intent.getStringExtra(SearchManager.QUERY)
            setQuery(query ?: "")
        }
    }
}