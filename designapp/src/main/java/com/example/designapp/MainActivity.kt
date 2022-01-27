package com.example.designapp

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.ViewAnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.example.designapp.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var tts: TextToSpeech? = null

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

        binding.btnLanguage.setOnClickListener {
//            tts = TextToSpeech(this) { status ->
//                if (status == TextToSpeech.SUCCESS) {
//                    tts?.language = Locale("uz")
//                    tts?.speak("Salom. Yaxshimisiz? O'lkamizda qish fasli. G'alayon. Bodring", TextToSpeech.QUEUE_ADD, null, null)
//                }
//            }
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(
//                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//            )
//
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "or forever hold your peace!")
//            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("uz"))
//            startActivityForResult(intent, 123)

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            startActivityForResult(intent, 123)
        }

        binding.btnVibrate.setOnClickListener {
            val vibrationService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (vibrationService.hasVibrator()) {
                val pattern = longArrayOf(1000, 2000, 4000, 8000, 16000)
                vibrationService.vibrate(pattern, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val confidence = data?.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)

            Log.i("TTTT", "${confidence?.joinToString()} $results")
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
    }
}