package com.example.sqllite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.btnLoad.setOnClickListener {
            val cursor = contentResolver.query(
                MyHoardContentProvider.CONTENT_URI,
                null, null, null, null,
            )
            cursor?.use {
                if (cursor.columnCount > 0) {
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
        }
    }
}