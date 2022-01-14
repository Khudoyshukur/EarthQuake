package com.example.downloadmanager.presentation

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.downloadmanager.data.model.ModelFile
import com.example.downloadmanager.databinding.ActivityMainBinding
import com.example.downloadmanager.databinding.DialogAddUrlBinding

class MainActivity : AppCompatActivity(), Callback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var downloadsAdapter: DownloadsAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() {
        downloadsAdapter = DownloadsAdapter()
        downloadsAdapter.setCallbackListener(this)
        binding.recyclerView.adapter = downloadsAdapter

        binding.addUrlBtn.setOnClickListener { addUrl() }
    }

    private fun subscribeToLiveData() {
        viewModel.files.observe(this) {
            downloadsAdapter.submitList(it)
        }
    }

    private fun addUrl() {
        var dialog: Dialog? = null
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            val dialogBinding = DialogAddUrlBinding.inflate(layoutInflater, binding.root, false)
            setView(dialogBinding.root)
            dialogBinding.add.setOnClickListener {
                if (dialogBinding.edittext.text.isNotBlank()) {
                    viewModel.addUrl(dialogBinding.edittext.text.toString())
                    dialog?.dismiss()
                }
            }
            dialogBinding.cancel.setOnClickListener { dialog?.dismiss() }

            dialog = create()
            dialog?.show()
        }
    }

    override fun onCancelDownload(downloadId: Long?) {
        viewModel.cancelDownload(downloadId ?: return)
    }

    override fun downloadFile(modelFile: ModelFile) {
        viewModel.downloadFile(modelFile)
    }

    override fun openFile(modelFile: ModelFile) {
        Toast.makeText(this, "open file", Toast.LENGTH_LONG).show()
    }

    override fun deleteFile(modelFile: ModelFile) {
        viewModel.deleteFile(modelFile)
    }
}