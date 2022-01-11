package com.example.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.text.NumberFormat

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class PriceTextView : AppCompatTextView {

    var price: Float = 0f
        set(value) {
            text = CURRENCY_FORMAT.format(value)
            field = value
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        extractAttrs(context, 0, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        extractAttrs(context, defStyle, attrs)
    }

    private fun extractAttrs(context: Context, defStyle: Int, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PriceTextView,
            defStyle,
            0
        )

        if (typedArray.hasValue(R.styleable.PriceTextView_price)) {
            price = typedArray.getFloat(R.styleable.PriceTextView_price, 0f)
        }

        typedArray.recycle()
    }

    companion object {
        private val CURRENCY_FORMAT: NumberFormat = NumberFormat.getCurrencyInstance()
    }
}
