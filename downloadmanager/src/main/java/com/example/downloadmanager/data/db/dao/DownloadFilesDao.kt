package com.example.downloadmanager.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.downloadmanager.data.model.ModelFile

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
abstract class DownloadFilesDao {
    @Insert
    abstract fun insertFile(file: ModelFile)

    @Update
    abstract fun updateFile(file: ModelFile)

    @Delete
    abstract fun deleteFile(file: ModelFile)

    @Query("select * from ModelFile")
    abstract fun getAllFilesLiveData(): LiveData<List<ModelFile>>

    @Query("select * from ModelFile")
    abstract fun getAllFiles(): List<ModelFile>

    @Query("update ModelFile set downloadId=:downloadId where id=:id")
    abstract fun updateDownloadId(id: Long?, downloadId: Long)

    @Query("select * from ModelFile where id=:id")
    abstract fun getFileById(id: Long?): List<ModelFile>

    @Query("delete from ModelFile where id=:id")
    abstract fun deleteFileById(id: Long?)

    @Query("select * from ModelFile where downloadId=:downloadId")
    abstract fun getFileByDownloadId(downloadId: Long): List<ModelFile>

    val TABLE_MODEL_FILE = arrayOf("ModelFile")
}