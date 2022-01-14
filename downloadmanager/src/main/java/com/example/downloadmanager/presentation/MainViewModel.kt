package com.example.downloadmanager.presentation

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.InvalidationTracker
import com.example.downloadmanager.data.db.dao.Database
import com.example.downloadmanager.data.model.FileStatus
import com.example.downloadmanager.data.model.ModelFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val db get() = Database.getInstance(app.applicationContext)

    private val _files = MutableLiveData<List<ModelFile>>()
    val files: LiveData<List<ModelFile>> get() = _files

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _files.postValue(db.downloadFilesDao.getAllFiles())
        }
        launchObservers()
    }

    private fun launchObservers() = viewModelScope.launch(Dispatchers.IO) {
        val files = db.downloadFilesDao.getAllFiles().filter { it.downloadId != null }
        for (file in files) {
            if (file.status != FileStatus.DOWNLOADED && file.status != FileStatus.FAILED && file.filePath == null) {
                launchStatusObserver(file.id, file.downloadId!!)
            }
        }

        db.invalidationTracker.addObserver(object :
            InvalidationTracker.Observer(db.downloadFilesDao.TABLE_MODEL_FILE) {
            override fun onInvalidated(tables: MutableSet<String>) {
                _files.postValue(db.downloadFilesDao.getAllFiles())
            }
        })
    }

    fun addUrl(url: String) = viewModelScope.launch(Dispatchers.IO) {
        db.downloadFilesDao.insertFile(
            ModelFile(
                fileName = "Random file name ${(0..1000000).random()}",
                url = url
            )
        )
    }

    fun downloadFile(modelFile: ModelFile) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val fileName = URLUtil.guessFileName(modelFile.url, null, null)
            val downloadPath =
                app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            val file = File(downloadPath, fileName)
            val request = DownloadManager.Request(Uri.parse(modelFile.url))
                .setTitle(modelFile.fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))

            val downloadManager = app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            db.downloadFilesDao.updateDownloadId(modelFile.id, downloadId)

            launchStatusObserver(modelFile.id, downloadId)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    app.applicationContext,
                    "${modelFile.url} is not valid url",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun launchStatusObserver(fileId: Long?, downloadId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            val modelFile = MutableStateFlow(db.downloadFilesDao.getFileById(fileId).first())
            val collectJob = viewModelScope.launch(Dispatchers.IO) {
                modelFile.collect {
                    db.downloadFilesDao.updateFile(it)
                }
            }

            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query()
                query.setFilterById(downloadId)

                val downloadManager =
                    app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                downloadManager.query(query).use { cursor ->
                    if (cursor.count == 0) {
                        return@use
                    }
                    cursor.moveToFirst()
                    val downloadedIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val bytesDownloaded = cursor.getFloat(downloadedIndex)

                    val totalBytesIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val totalBytes = cursor.getFloat(totalBytesIndex)

                    val progress = ((bytesDownloaded / totalBytes) * 100).toInt()
                    val status = getFileStatus(cursor)

                    val filePath = if (status == FileStatus.DOWNLOADED) {
                        val indexFileId = cursor.getColumnIndex(DownloadManager.COLUMN_ID)
                        val fId = cursor.getLong(indexFileId)
                        downloadManager.getUriForDownloadedFile(fId).path
                    } else {
                        modelFile.value.filePath
                    }

                    modelFile.emit(
                        modelFile.value.copy(
                            progress = progress,
                            status = status,
                            filePath = filePath
                        )
                    )

                    if (status == FileStatus.FAILED || status == FileStatus.IDLE) {
                        downloading = false
                    }
                }
                delay(500)
            }

            collectJob.cancel()
        }

    private fun getFileStatus(cursor: Cursor): FileStatus {
        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

        return when (cursor.getInt(statusIndex)) {
            DownloadManager.STATUS_SUCCESSFUL -> FileStatus.DOWNLOADED
            DownloadManager.STATUS_PAUSED -> FileStatus.PAUSED
            DownloadManager.STATUS_PENDING -> FileStatus.PENDING
            DownloadManager.STATUS_FAILED -> FileStatus.FAILED
            DownloadManager.STATUS_RUNNING -> FileStatus.RUNNING
            else -> FileStatus.IDLE
        }
    }

    fun cancelDownload(downloadId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val modelFile = db.downloadFilesDao.getFileByDownloadId(downloadId)
        if (modelFile.isNotEmpty()) {
            db.downloadFilesDao.updateFile(
                modelFile.first().copy(
                    downloadId = null,
                    status = FileStatus.IDLE,
                    filePath = null
                )
            )
        }

        val downloadManager = app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId)
    }

    fun deleteFile(modelFile: ModelFile) = viewModelScope.launch(Dispatchers.IO) {
        db.downloadFilesDao.deleteFileById(modelFile.id)
    }
}