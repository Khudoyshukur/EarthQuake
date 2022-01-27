package com.example.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CompassView : View {

    private lateinit var circlePaint: Paint
    private lateinit var markerPaint: Paint
    private lateinit var textPaint: Paint

    private lateinit var northText: String
    private lateinit var southText: String
    private lateinit var eastText: String
    private lateinit var westText: String

    private var textHeight: Float = 0f

    private var bearing: Float? = null

    private var mPitch: Float = 0f
    private var mRoll: Float = 0f

    private lateinit var borderGradientColors: IntArray
    private lateinit var borderGradientPositions: FloatArray
    private lateinit var glassGradientColors: IntArray
    private lateinit var glassGradientPositions: FloatArray

    private var skyHorizonColorFrom: Int = 0
    private var skyHorizonColorTo: Int = 0
    private var groundHorizonColorFrom: Int = 0
    private var groundHorizonColorTo: Int = 0

    fun getPitch() = mPitch
    fun setPitch(pitch: Float) {
        mPitch = pitch

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
    }

    fun getRoll() = mRoll
    fun setRoll(roll: Float) {
        mRoll = roll

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
    }

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

        markerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        markerPaint.color = Color.WHITE
        markerPaint.alpha = 200
        markerPaint.strokeWidth = 1f
        markerPaint.style = Paint.Style.STROKE
        markerPaint.setShadowLayer(
            2f, 1f, 1f, ContextCompat.getColor(
                context,
                R.color.shadow_color
            )
        )

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.WHITE
        textHeight = 30f
        textPaint.textSize = textHeight
        textPaint.isFakeBoldText = true
        textPaint.isSubpixelText = true
        textPaint.textAlign = Paint.Align.LEFT

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

        borderGradientColors = IntArray(4)
        borderGradientPositions = FloatArray(4)
        borderGradientColors[3] = ContextCompat.getColor(context, R.color.outer_border)
        borderGradientColors[2] = ContextCompat.getColor(context, R.color.inner_border_one)
        borderGradientColors[1] = ContextCompat.getColor(context, R.color.inner_border_two)
        borderGradientColors[0] = ContextCompat.getColor(context, R.color.inner_border)
        borderGradientPositions[3] = 0.0f
        borderGradientPositions[2] = 1 - 0.03f
        borderGradientPositions[1] = 1 - 0.06f
        borderGradientPositions[0] = 1.0f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Log.i("TTTT", event?.pressure.toString())

        return true
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
            canvas.drawLine(px, 0f, px, 10f, markerPaint)
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
                            markerPaint
                        )
                        canvas.drawLine(
                            px,
                            2 * textHeight,
                            px + 5,
                            3 * textHeight,
                            markerPaint
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

                val textWidth = markerPaint.measureText(dirString)
                canvas.drawText(dirString, px - textWidth / 2f, textHeight, textPaint)
            } else if (i % 3 == 0) {
                val angle = (i * 15).toString()
                val angleTextWidth = markerPaint.measureText(angle)

                val angleTextX = px - angleTextWidth / 2
                val angleTextY = py - radius + textHeight
                canvas.drawText(angle, angleTextX, angleTextY, textPaint)
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