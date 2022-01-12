package com.example.starsign

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.starsign.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mContract get() = ActivityResultContracts.StartActivityForResult()
    private val selectStarLauncher = registerForActivityResult(mContract) {
        if (it.resultCode == Activity.RESULT_OK) {
            val star = it.data?.getStringExtra("extra_selected_star") ?: "no selected"
            Toast.makeText(this, star, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectStar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = Uri.parse("starsigns://")
            selectStarLauncher.launch(intent)
        }
    }
}