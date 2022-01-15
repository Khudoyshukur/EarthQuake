package com.example.earthquakeapplication.provider

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.earthquakeapplication.database.Database
import com.example.earthquakeapplication.database.EarthquakeDAO

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthquakeSearchProvider : ContentProvider() {

    private lateinit var uriMatcher: UriMatcher

    private var earthquakeDao: EarthquakeDAO? = null

    override fun onCreate(): Boolean {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGESTIONS)
        uriMatcher.addURI(
            AUTHORITY,
            "${SearchManager.SUGGEST_URI_PATH_QUERY}/*",
            SEARCH_SUGGESTIONS
        )
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH_SUGGESTIONS)
        uriMatcher.addURI(
            AUTHORITY,
            "${SearchManager.SUGGEST_URI_PATH_SHORTCUT}/*",
            SEARCH_SUGGESTIONS
        )

        context?.let {
            earthquakeDao = Database.getInstance(it).earthquakeDAO
        }

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        if (uriMatcher.match(uri) == SEARCH_SUGGESTIONS) {
            val query = uri.lastPathSegment
            return earthquakeDao?.generateSearchSuggestion("%$query%")
        }

        return null
    }

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        SEARCH_SUGGESTIONS -> {
            SearchManager.SUGGEST_MIME_TYPE
        }
        else -> throw IllegalArgumentException("Invalid uri: $uri")
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    companion object {
        const val AUTHORITY = "com.example.earthquakeapplication.provider.earthquake/"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY")

        private const val SEARCH_SUGGESTIONS = 1
    }
}