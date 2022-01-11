package com.example.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.customviews.databinding.ClearableEdittextBinding

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class ClearableEdittext : LinearLayout {

    private lateinit var binding: ClearableEdittextBinding

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        orientation = VERTICAL

        val infService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(infService) as LayoutInflater

        binding = ClearableEdittextBinding.inflate(layoutInflater, this, true)

        hookUpButton()
    }

    private fun hookUpButton() {
        binding.clearBtn.setOnClickListener {
            binding.editText.text.clear()
        }
    }
}