package com.example.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.res.ResourcesCompat

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CompassView : View {

    private lateinit var circlePaint: Paint
    private lateinit var whitePaint: Paint

    private lateinit var northText: String
    private lateinit var southText: String
    private lateinit var eastText: String
    private lateinit var westText: String

    private var textHeight: Float = 0f

    private var bearing: Float? = null

    fun setBearing(bearing: Float) {
        this.bearing = bearing
        invalidate()

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        super.dispatchPopulateAccessibilityEvent(event)
        return if (isShown) {
            event?.text?.add(bearing.toString())
            true
        } else {
            false
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.color = ResourcesCompat.getColor(resources, R.color.compass_background, null)
        circlePaint.strokeWidth = 1f
        circlePaint.style = Paint.Style.FILL_AND_STROKE

        whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.color = Color.WHITE
        textHeight = 30f
        whitePaint.textSize = textHeight

        northText = context.getString(R.string.north)
        southText = context.getString(R.string.south)
        eastText = context.getString(R.string.east)
        westText = context.getString(R.string.west)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CompassView,
                defStyleAttr,
                0
            )

            if (typedArray.hasValue(R.styleable.CompassView_bearing)) {
                bearing = typedArray.getFloat(R.styleable.CompassView_bearing, 0f)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val px = measuredWidth / 2f
        val py = measuredHeight / 2f

        val radius = px.coerceAtMost(py)

        canvas.drawCircle(px, py, radius, circlePaint)
        bearing?.let {
            canvas.rotate(it * (-1), px, py)
        }
        canvas.save()

        for (i in 0 until 24) {
            canvas.drawLine(px, 0f, px, 10f, whitePaint)
            canvas.save()
            canvas.translate(0f, textHeight)

            if (i % 6 == 0) {
                var dirString = ""
                when (i) {
                    0 -> {
                        canvas.drawLine(
                            px,
                            2 * textHeight,
                            px - 5,
                            3 * textHeight,
                            whitePaint
                        )
                        canvas.drawLine(
                            px,
                            2 * textHeight,
                            px + 5,
                            3 * textHeight,
                            whitePaint
                        )
                        dirString = northText
                    }
                    6 -> {
                        dirString = eastText
                    }
                    12 -> {
                        dirString = southText
                    }
                    18 -> {
                        dirString = westText
                    }
                }

                val textWidth = whitePaint.measureText(dirString)
                canvas.drawText(dirString, px - textWidth / 2f, textHeight, whitePaint)
            } else if (i % 3 == 0) {
                val angle = (i * 15).toString()
                val angleTextWidth = whitePaint.measureText(angle)

                val angleTextX = px - angleTextWidth / 2
                val angleTextY = py - radius + textHeight
                canvas.drawText(angle, angleTextX, angleTextY, whitePaint)
            }
            canvas.restore()

            canvas.rotate(15f, px, py)
        }

        canvas.restore()

        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredHeight = measure(heightMeasureSpec)
        val measuredWidth = measure(widthMeasureSpec)

        val d = measuredHeight.coerceAtMost(measuredWidth)

        setMeasuredDimension(d, d)
    }

    private fun measure(measureSpec: Int): Int {

        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        // default size of the view
        return when (specMode) {
            MeasureSpec.UNSPECIFIED -> {
                200
            }
            else -> {
                /**
                 * use specSize when mode is either MeasureSpec.AT_MOST or MeasureSpec.EXACTLY
                 * */
                specSize
            }
        }
    }
}