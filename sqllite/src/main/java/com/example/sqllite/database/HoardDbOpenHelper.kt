package com.example.sqllite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class HoardDbOpenHelper(
    context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // simply drop table and create new one
        database?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    companion object {
        const val TABLE_NAME = "GoldHoards"
        const val DATABASE_NAME = "HoardsDatabase.db"
        const val DATABASE_VERSION = 1
        private const val DATABASE_CREATE =
            """CREATE TABLE $TABLE_NAME (
                ${HoardContract.KEY_ID} integer primary key autoincrement, 
                ${HoardContract.KEY_GOLD_HOARD_NAME_COLUMN} text not null, 
                ${HoardContract.KEY_GOLD_HOARDED_COLUMN} float, 
                ${HoardContract.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN} integer
            );"""
    }
}