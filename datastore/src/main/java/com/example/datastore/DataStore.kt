package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

/**
 * Preferences DataStore example
 * Reading from datastore and writing to datastore
 * */

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

val EXAMPLE_INPUT_KEY = stringPreferencesKey("example_input_key")
fun getExampleFlow(context: Context): Flow<String> {
    return context.preferencesDataStore.data.map { preferences ->
        preferences[EXAMPLE_INPUT_KEY] ?: ""
    }
}

suspend fun setExampleInput(context: Context, input: String) {
    context.preferencesDataStore.edit {
        it[EXAMPLE_INPUT_KEY] = input
    }
}

/**
 * Proto DataStore
 * */