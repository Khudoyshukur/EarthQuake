package com.example.downloadmanager.data.db

import androidx.room.TypeConverter
import com.example.downloadmanager.data.model.FileStatus

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TypeConverters {
    @TypeConverter
    fun fileStatusToString(fileStatus: FileStatus) = fileStatus.toString()

    @TypeConverter
    fun stringToFileStatus(fileStatus: String) = FileStatus.valueOf(fileStatus)
}