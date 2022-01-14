package com.example.sqllite

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.example.sqllite.databinding.ActivitySearchTestBinding

class SearchTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchableInfo = searchManager.getSearchableInfo(componentName)

        val searchView = menu?.findItem(R.id.search_view)?.actionView as? SearchView
        searchView?.setSearchableInfo(searchableInfo)
        searchView?.isIconifiedByDefault = true

        return true
    }
}