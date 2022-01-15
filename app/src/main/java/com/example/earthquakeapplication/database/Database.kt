package com.example.earthquakeapplication.database

import android.content.Context
import androidx.room.Database as DB
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.earthquakeapplication.model.EarthQuake

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@DB(entities = [EarthQuake::class], version = 1)
@TypeConverters(com.example.earthquakeapplication.database.TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract val earthquakeDAO: EarthquakeDAO

    companion object {
        private var instance: Database? = null

        fun getInstance(context: Context): Database {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(context, Database::class.java, "earthquake.db")
                    .build()
            }

            return instance!!
        }
    }
}