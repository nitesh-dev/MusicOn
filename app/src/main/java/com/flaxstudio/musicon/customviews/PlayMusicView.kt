package com.flaxstudio.musicon.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.utils.Vector2
import com.flaxstudio.musicon.utils.toPx
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PlayMusicView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val defaultSeekBarTrackColor = Color.BLACK
    private val defaultSeekBarProgressTrackColor = Color.RED

    private var viewSize = 0f
    private var canvasCenter = 0f
    private var playDiscRadius = 0f
    private var seekBarRadius = 0f
    private var progressValue = 45f                         // max 1000
    private var seekBarProgressAngle = 0f
    private val seekBarThumbPos = Vector2()
    private val seekBarThumbRadius = 5.toPx.toFloat()


    // paints
    private val seekBarTrackPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2.toPx.toFloat()
        color = defaultSeekBarTrackColor
    }

    private val seekBarProgressTrackPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4.toPx.toFloat()
    }

    private val seekBarThumbPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }


    private val playDiscPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED
    }

    init {
        setAttributes(attrs)
    }

    private fun setAttributes(attrs: AttributeSet){
        // getting all attributes from xml
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayMusicView, 0,0)

        // extracting values
        seekBarTrackPaint.color = typedArray.getColor(R.styleable.PlayMusicView_seekBarTrackColor, defaultSeekBarTrackColor)
        seekBarProgressTrackPaint.color = typedArray.getColor(R.styleable.PlayMusicView_seekBarProgressTrackColor, defaultSeekBarProgressTrackColor)
        seekBarThumbPaint.color = typedArray.getColor(R.styleable.PlayMusicView_seekBarProgressTrackColor, defaultSeekBarProgressTrackColor)

        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = measuredWidth.coerceAtMost(measuredHeight)

        viewSize = size.toFloat()
        canvasCenter = viewSize * 0.5f
        playDiscRadius = (size - paddingLeft - paddingRight) * 0.35f
        seekBarRadius = (size - paddingLeft - paddingRight) * 0.45f

        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        seekBarProgressAngle = progressValue / 1000 * 360
        calculateThumbPosition()

        canvas.drawCircle(canvasCenter, canvasCenter, seekBarRadius, seekBarTrackPaint)
        //canvas.drawCircle(canvasCenter, canvasCenter, playDiscRadius, playDiscPaint)

        canvas.drawArc(canvasCenter - seekBarRadius , canvasCenter - seekBarRadius, canvasCenter + seekBarRadius, canvasCenter + seekBarRadius, -90f, seekBarProgressAngle, false, seekBarProgressTrackPaint)
        canvas.drawCircle(seekBarThumbPos.x, seekBarThumbPos.y, seekBarThumbRadius, seekBarThumbPaint)
    }

    private fun calculateThumbPosition(){

        val angleRad = (90 - seekBarProgressAngle) * PI / 180
        val x = canvasCenter + seekBarRadius * cos(angleRad).toFloat()
        val y = canvasCenter - seekBarRadius * sin(angleRad).toFloat()
        seekBarThumbPos.setValue(x, y)
    }


    // public functions
    fun setProgressValue(value: Int){
        progressValue = value.toFloat()
        invalidate()
    }
}