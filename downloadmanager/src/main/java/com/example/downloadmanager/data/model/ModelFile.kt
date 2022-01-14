package com.example.downloadmanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity
data class ModelFile(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "downloadId")
    var downloadId: Long? = null,

    @ColumnInfo(name = "fileName")
    val fileName: String? = null,

    @ColumnInfo(name = "url")
    val url: String = "",

    @ColumnInfo(name = "file_size")
    val fileSize: Long = 0,

    @ColumnInfo(name = "progress")
    val progress: Int = 0,

    @ColumnInfo(name = "file_path")
    var filePath: String? = null,

    @ColumnInfo(name = "status_string")
    val statusString: String = "",

    @ColumnInfo(name = "status")
    var status: FileStatus = FileStatus.IDLE
)

enum class FileStatus {
    RUNNING, FAILED, DOWNLOADED, IDLE, PAUSED, PENDING
}