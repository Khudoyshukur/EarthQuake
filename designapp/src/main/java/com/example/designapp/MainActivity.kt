package com.example.designapp

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewAnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.example.designapp.databinding.ActivityMainBinding
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToClick.setOnClickListener {
            if (binding.view.isVisible) {
                hideView()
            } else {
                showView()
            }
        }

        binding.btnActivity.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)

            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                androidx.core.util.Pair(binding.image, ViewCompat.getTransitionName(binding.image))
            ).toBundle()

            startActivity(intent, bundle)
        }
    }

    private fun hideView() {
        val centerX = binding.view.width / 2
        val centerY = binding.view.height / 2

        val coveringRadius = hypot(centerX.toDouble(), centerY.toDouble())

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.view,
            centerX,
            centerY,
            coveringRadius.toFloat(),
            0f
        )

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                binding.view.isVisible = false
            }

            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        anim.start()
    }

    private fun showView() {
        val centerX = binding.view.width / 2
        val centerY = binding.view.height / 2

        val coveringRadius = hypot(centerX.toDouble(), centerY.toDouble())

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.view,
            centerX,
            centerY,
            0f,
            coveringRadius.toFloat()
        )

        binding.view.isVisible = true
        anim.start()
    }
}