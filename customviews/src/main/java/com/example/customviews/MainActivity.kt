package com.example.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customviews.databinding.ActivityMainBinding

/**
 *
 * The best approach to use when creating new View depends on what you want to achieve:
 *
 * 1-> Modify or extend the appearance and/or behaviour of an existing View
 * 2-> Combine Views
 * 3-> Create an entirely new control
 *
 * */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.increaseBtn.setOnClickListener {
            binding.priceTextView.price = binding.priceTextView.price + 0.1f
        }

        binding.randomBearingBtn.setOnClickListener {
            val randomBearing = (0..360).random().toFloat()
            binding.compassView.setBearing(randomBearing)
        }
    }
}