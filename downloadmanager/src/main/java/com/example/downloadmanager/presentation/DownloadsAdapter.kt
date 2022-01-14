package com.example.downloadmanager.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.downloadmanager.R
import com.example.downloadmanager.data.model.FileStatus
import com.example.downloadmanager.data.model.ModelFile
import com.example.downloadmanager.databinding.ItemDownloadBinding

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class DownloadsAdapter : ListAdapter<ModelFile, DownloadsAdapter.ViewHolder>(Util()) {

    private var callback: Callback? = null
    fun setCallbackListener(callback: Callback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDownloadBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemDownloadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: ModelFile) {
            binding.name.text = file.fileName

            binding.downloadProgress.isVisible = file.status == FileStatus.RUNNING
            binding.existStatus.isVisible = file.status != FileStatus.RUNNING

            binding.statusText.text = file.statusString
            binding.downloadProgress.progress = file.progress

            binding.downloadProgress.setOnClickListener {
                callback?.onCancelDownload(file.downloadId)
            }

            binding.deleteFile.setOnClickListener {
                callback?.deleteFile(file)
            }

            with(file.status == FileStatus.DOWNLOADED) {
                binding.existStatus.setImageDrawable(
                    if (this) {
                        ContextCompat.getDrawable(binding.root.context, R.drawable.ic_play)
                    } else {
                        ContextCompat.getDrawable(binding.root.context, R.drawable.ic_download)
                    }
                )

                binding.existStatus.setOnClickListener {
                    if (this) {
                        callback?.openFile(file)
                    } else {
                        callback?.downloadFile(file)
                    }
                }
            }
        }
    }

    class Util : DiffUtil.ItemCallback<ModelFile>() {
        override fun areItemsTheSame(oldItem: ModelFile, newItem: ModelFile) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ModelFile, newItem: ModelFile) =
            oldItem == newItem
    }
}

interface Callback {
    fun onCancelDownload(downloadId: Long?)
    fun downloadFile(modelFile: ModelFile)
    fun openFile(modelFile: ModelFile)
    fun deleteFile(modelFile: ModelFile)
}