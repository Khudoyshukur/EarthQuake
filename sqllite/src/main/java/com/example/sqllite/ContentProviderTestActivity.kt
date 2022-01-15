package com.example.sqllite

import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.sqllite.database.HoardContract
import com.example.sqllite.databinding.ActivityContentProviderTestBinding
import com.example.sqllite.entity.Hoard
import com.example.sqllite.provider.MyHoardContentProvider

class ContentProviderTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentProviderTestBinding
    private lateinit var hoardsAdapter: HoardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentProviderTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        hoardsAdapter = HoardsAdapter()
        binding.recyclerView.adapter = hoardsAdapter

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, MyContentSearchLoader())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, MyContentSearchLoader())
    }

    private inner class MyContentSearchLoader : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

            if (intent.action == Intent.ACTION_VIEW) {
                val selection = "${HoardContract.KEY_ID}=?"
                val selectionArgs = arrayOf(intent.data?.lastPathSegment)
                return CursorLoader(
                    this@ContentProviderTestActivity, MyHoardContentProvider.CONTENT_URI,
                    null, selection, selectionArgs, null
                )
            }

            val extraName = intent?.getStringExtra(SearchManager.QUERY)
            if (!extraName.isNullOrBlank()) {
                val selection = "${HoardContract.KEY_GOLD_HOARD_NAME_COLUMN} like \'%$extraName%\'"

                return CursorLoader(
                    this@ContentProviderTestActivity, MyHoardContentProvider.CONTENT_URI,
                    null, selection, null, null
                )
            }

            return CursorLoader(
                this@ContentProviderTestActivity, MyHoardContentProvider.CONTENT_URI,
                null, null, null, null
            )
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            data?.use { cursor ->
                val hoards = arrayListOf<Hoard>()
                while (cursor.moveToNext()) {
                    val indexId = cursor.getColumnIndex(HoardContract.KEY_ID)
                    val indexName =
                        cursor.getColumnIndex(HoardContract.KEY_GOLD_HOARD_NAME_COLUMN)
                    val indexGoldHoarded =
                        cursor.getColumnIndex(HoardContract.KEY_GOLD_HOARDED_COLUMN)
                    val indexAccessible =
                        cursor.getColumnIndex(HoardContract.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN)

                    hoards.add(
                        Hoard(
                            id = cursor.getLong(indexId),
                            hoardName = cursor.getString(indexName),
                            isHoardAccessible = cursor.getInt(indexAccessible) != 0,
                            goldHoarded = cursor.getInt(indexGoldHoarded)
                        )
                    )
                }

                hoardsAdapter.submitList(hoards)
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {}
    }

    companion object {
        private const val LOADER_ID = 123
    }
}