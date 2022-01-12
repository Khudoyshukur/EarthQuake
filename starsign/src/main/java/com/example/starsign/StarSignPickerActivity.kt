package com.example.starsign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starsign.databinding.ActivityStarSignPickerBinding

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class StarSignPickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarSignPickerBinding
    private lateinit var sAdapter: StarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarSignPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sAdapter = StarsAdapter(::onStarSelected)
        binding.root.adapter = sAdapter

        setStars()
    }

    private fun setStars() {
        sAdapter.submitList(
            listOf(
                "Abdulloh ibn Mas'ud",
                "Abu Bakr",
                "Umar",
                "Usmon",
                "Ali",
                "Zayd",
                "Ubay ibn Kab",
                "Abdulloh ibn Umar",
                "Solmon forsiy"
            )
        )
    }

    private fun onStarSelected(star: String) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_STAR, star)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val EXTRA_SELECTED_STAR = "extra_selected_star"
    }
}