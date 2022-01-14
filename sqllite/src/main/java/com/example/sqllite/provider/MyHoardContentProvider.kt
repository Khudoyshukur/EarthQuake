package com.example.sqllite.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.os.CancellationSignal
import android.text.TextUtils
import com.example.sqllite.database.HoardContract
import com.example.sqllite.database.HoardDbOpenHelper
import java.lang.IllegalArgumentException

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class MyHoardContentProvider : ContentProvider() {

    private var helper: HoardDbOpenHelper? = null
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun onCreate(): Boolean {
        helper = HoardDbOpenHelper(
            context!!,
            HoardDbOpenHelper.DATABASE_NAME,
            null,
            HoardDbOpenHelper.DATABASE_VERSION
        )
        uriMatcher.addURI(CONTENT_AUTHORITY, "lairs", ALL_ROWS)
        uriMatcher.addURI(CONTENT_AUTHORITY, "lairs/#", SINGLE_ROW)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return query(uri, projection, selection, selectionArgs, sortOrder, null)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?
    ): Cursor? {
        val database = try {
            helper?.writableDatabase
        } catch (e: SQLiteException) {
            helper?.readableDatabase
        }

        val groupBy: String? = null
        val having: String? = null

        val queryBuilder = SQLiteQueryBuilder()

        when (uriMatcher.match(uri)) {
            SINGLE_ROW -> {
                val rowId = uri.lastPathSegment
                queryBuilder.appendWhere("${HoardContract.KEY_ID}=$rowId")
            }
        }

        queryBuilder.tables = HoardDbOpenHelper.TABLE_NAME

        val limit: String? = null

        val cursor = queryBuilder.query(
            database,
            projection,
            selection,
            selectionArgs,
            groupBy,
            having,
            sortOrder,
            limit,
            cancellationSignal
        )

        return cursor
    }

    override fun getType(uri: Uri): String = when (uriMatcher.match(uri)) {
        SINGLE_ROW -> "vnd.android.cursor.item/vmd.example.lairs"
        ALL_ROWS -> "vnd.android.cursor.dir/vmd.example.lairs"
        else -> throw IllegalArgumentException("UNSUPPORTED URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val database = helper?.writableDatabase

        val nullColumnHack: String? = null

        val id = database?.insert(HoardDbOpenHelper.TABLE_NAME, nullColumnHack, values) ?: -1

        if (id > -1) {
            val insertedUri = ContentUris.withAppendedId(uri, id)
            context?.contentResolver?.notifyChange(insertedUri, null)
            return insertedUri
        }

        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = helper?.writableDatabase

        val affectedRowsCount = if (uriMatcher.match(uri) == SINGLE_ROW) {
            val rowId = uri.lastPathSegment
            val changedSelection =
                "${HoardContract.KEY_ID}=$rowId${if (!TextUtils.isEmpty(selection)) " and $selection" else ""}"
            database?.delete(HoardDbOpenHelper.TABLE_NAME, changedSelection, selectionArgs)
        } else {
            database?.delete(HoardDbOpenHelper.TABLE_NAME, selection ?: "1", selectionArgs)
        } ?: -1

        if (affectedRowsCount > -1) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return affectedRowsCount
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val database = helper?.writableDatabase

        val affectedRowsCount = if (uriMatcher.match(uri) == SINGLE_ROW) {
            val rowId = uri.lastPathSegment
            val changedSelection =
                "${HoardContract.KEY_ID}=$rowId${if (!TextUtils.isEmpty(selection)) " and $selection" else ""}"
            database?.update(HoardDbOpenHelper.TABLE_NAME, values, changedSelection, selectionArgs)
        } else {
            database?.update(HoardDbOpenHelper.TABLE_NAME, values, selection, selectionArgs)
        } ?: -1

        if (affectedRowsCount > -1) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return affectedRowsCount
    }

    companion object {
        const val CONTENT_AUTHORITY = "content://com.example.sqllite.provider.hoarder"
        val CONTENT_URI = Uri.parse(CONTENT_AUTHORITY)

        private const val ALL_ROWS = 1
        private const val SINGLE_ROW = 2
    }
}