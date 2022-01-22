package com.example.earthquakeapplication.database

import android.app.SearchManager
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.earthquakeapplication.model.EarthQuake

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(earthquakes: List<EarthQuake>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(earthQuake: EarthQuake)

    @Delete
    fun deleteAll(earthquakes: List<EarthQuake>)

    @Delete
    fun delete(earthQuake: EarthQuake)

    @Update
    fun updateAll(earthquakes: List<EarthQuake>)

    @Update
    fun update(earthQuake: EarthQuake)

    @Query("select * from EarthQuake")
    fun getALlEarthquakes(): LiveData<List<EarthQuake>>

    @Query("select * from EarthQuake")
    fun getALlEarthquakesBlocking(): List<EarthQuake>

    @Query("select mId as _id, mDetails as ${SearchManager.SUGGEST_COLUMN_TEXT_1} from EarthQuake where mDetails like :query order by mDate DESC")
    fun generateSearchSuggestion(query: String): Cursor

    @Query("select * from EarthQuake where mDetails like :query order by mDate DESC")
    fun searchEarthquakes(query: String): LiveData<List<EarthQuake>>

    @Query("select * from EarthQuake where mId=:id limit 1")
    fun getEarthquake(id: String): LiveData<EarthQuake>
}