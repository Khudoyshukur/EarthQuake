package com.example.earthquakeapplication.database

import android.location.Location
import androidx.room.TypeConverter
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TypeConverters {
    @TypeConverter
    fun dateToLong(date: Date) = date.time

    @TypeConverter
    fun longToDate(long: Long) = Date(long)

    @TypeConverter
    fun locationToString(location: Location) = "${location.latitude} ${location.longitude}"

    @TypeConverter
    fun stringToLocation(string: String): Location {
        val latLng = string.split(" ")
        return Location("gps").also {
            it.latitude = latLng[0].toDouble()
            it.longitude = latLng[1].toDouble()
        }
    }
}