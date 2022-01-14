package com.example.sqllite

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.sqllite.databinding.ActivityMainBinding
import com.example.sqllite.databinding.DialogHoardNameBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var hoardsAdapter: HoardsAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() {
        hoardsAdapter = HoardsAdapter()
        binding.recyclerView.adapter = hoardsAdapter

        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.getAllHoards().observe(this) {
            hoardsAdapter.submitList(it)
        }
    }

    private fun showAddDialog() {
        var dialog: Dialog? = null
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            val dialogBinding = DialogHoardNameBinding.inflate(layoutInflater, binding.root, false)
            setView(dialogBinding.root)

            dialogBinding.cancel.setOnClickListener {
                dialog?.dismiss()
            }

            dialogBinding.add.setOnClickListener {
                if (dialogBinding.edittext.text.isNotBlank()) {
                    viewModel.addHoard(dialogBinding.edittext.text.toString())
                    dialog?.dismiss()
                }
            }

            dialog = create()
            dialog?.show()
        }
    }
}