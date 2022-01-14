package com.example.downloadmanager.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.downloadmanager.data.db.TypeConverters
import com.example.downloadmanager.data.model.ModelFile

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@androidx.room.Database(entities = [ModelFile::class], version = 1)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract val downloadFilesDao: DownloadFilesDao

    companion object {
        private var databaseInstance: Database? = null

        fun getInstance(context: Context): Database {
            if (databaseInstance == null) {
                databaseInstance =
                    Room.databaseBuilder(context, Database::class.java, "database.db")
                        .build()
            }

            return databaseInstance!!
        }
    }
}