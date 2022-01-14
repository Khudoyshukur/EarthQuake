package com.example.sqllite.database

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sqllite.entity.Hoard

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object HoardDatabase {
    private var databaseHelper: HoardDbOpenHelper? = null

    private val databaseChangeObservers = arrayListOf<Runnable>()

    fun initialize(context: Context) {
        if (databaseHelper == null) {
            databaseHelper = HoardDbOpenHelper(
                context,
                HoardDbOpenHelper.DATABASE_NAME,
                null,
                HoardDbOpenHelper.DATABASE_VERSION
            )
        }
    }

    fun insert(hoard: Hoard): Long {
        val values = hoard.getHoardContentValues()

        val database = databaseHelper?.writableDatabase

        val affectedRows = database?.insert(
            HoardDbOpenHelper.TABLE_NAME, null, values
        ) ?: 0
        if (affectedRows > 0) {
            databaseChangeObservers.forEach { it.run() }
        }

        return affectedRows
    }

    fun insertAll(hoards: List<Hoard>): Long {
        return hoards.sumOf { insert(it) }
    }

    fun delete(hoard: Hoard): Int {
        val where = "${HoardContract.KEY_ID}=?"
        val whereArgs = arrayOf(hoard.id.toString())

        val database = databaseHelper?.writableDatabase

        val affectedRows = database?.delete(HoardDbOpenHelper.TABLE_NAME, where, whereArgs) ?: 0
        if (affectedRows > 0) {
            databaseChangeObservers.forEach { it.run() }
        }

        return affectedRows
    }

    fun deleteAll(hoards: List<Hoard>): Int {
        return hoards.sumOf { delete(it) }
    }

    fun update(hoard: Hoard): Int {
        val values = hoard.getHoardContentValues()
        val where = "${HoardContract.KEY_ID}=?"
        val whereArgs = arrayOf(hoard.id.toString())

        val database = databaseHelper?.writableDatabase

        val affectedRows =
            database?.update(HoardDbOpenHelper.TABLE_NAME, values, where, whereArgs) ?: 0
        if (affectedRows > 0) {
            databaseChangeObservers.forEach { it.run() }
        }

        return affectedRows
    }

    fun updateAll(hoards: List<Hoard>): Int {
        return hoards.sumOf { update(it) }
    }

    fun getAllHoards(): List<Hoard> {
        val hoards = arrayListOf<Hoard>()

        val query = "select * from ${HoardDbOpenHelper.TABLE_NAME}"
        val database = databaseHelper?.writableDatabase
        val cursor = database?.rawQuery(query, null)
        cursor?.use {
            if (cursor.columnCount > 0) {
                while (cursor.moveToNext()) {
                    val indexId = cursor.getColumnIndex(HoardContract.KEY_ID)
                    val indexName = cursor.getColumnIndex(HoardContract.KEY_GOLD_HOARD_NAME_COLUMN)
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
            }
        }

        return hoards
    }

    fun getAllHoardsAsLiveData(): LiveData<List<Hoard>> {
        val liveData = MutableLiveData(getAllHoards())

        addDatabaseChangeObserver {
            liveData.value = getAllHoards()
        }

        return liveData
    }

    fun addDatabaseChangeObserver(runnable: Runnable) {
        databaseChangeObservers.add(runnable)
    }

    private fun Hoard.getHoardContentValues(): ContentValues {
        val values = ContentValues()
        values.put(HoardContract.KEY_GOLD_HOARD_NAME_COLUMN, hoardName)
        values.put(HoardContract.KEY_GOLD_HOARDED_COLUMN, goldHoarded)
        values.put(HoardContract.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN, isHoardAccessible)
        return values
    }
}