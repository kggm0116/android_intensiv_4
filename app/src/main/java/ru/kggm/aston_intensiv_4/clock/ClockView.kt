package ru.kggm.aston_intensiv_4.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import ru.kggm.aston_intensiv_4.utility.dip2px
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val FRAME_WIDTH_DP = 4
        private const val PADDING_DP = 16
        private const val SECOND_NOTCH_LENGTH_PERC = 0.08f
        private const val MINUTE_NOTCH_LENGTH_PERC = 0.16f
        private const val HOURS_TEXT_OFFSET_PERC = 0.2f
        private const val NOTCH_WIDTH_DP = 2

        private val NOTCH_ANGLES by lazy { (0 until 60).map { PI / 30 * it } }
    }

    private val customAttrs = ClockViewAttrs(context, attrs, defStyleAttr)

    private val padding by lazy { dip2px(PADDING_DP) }
    private val notchWidth by lazy { dip2px(NOTCH_WIDTH_DP) }

    private var notchesVisible = customAttrs.notchesVisible
    private var secondsHandVisible = customAttrs.secondsHandVisible
    private var minutesHandVisible = customAttrs.minutesHandVisible
    private var hoursHandVisible = customAttrs.hoursHandVisible
    private var hoursTextVisible = customAttrs.hoursTextVisible

    private val notchesPaint by lazy {
        Paint().apply {
            color = customAttrs.notchColor
            style = Paint.Style.STROKE
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val secondsHandPaint by lazy {
        Paint().apply {
            color = customAttrs.secondsHandColor
            style = Paint.Style.STROKE
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val minutesHandPaint by lazy {
        Paint().apply {
            color = customAttrs.minutesHandColor
            style = Paint.Style.STROKE
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val hoursHandPaint by lazy {
        Paint().apply {
            color = customAttrs.hoursHandColor
            style = Paint.Style.STROKE
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val hoursTextPaint by lazy {
        Paint().apply {
            color = customAttrs.hoursTextColor
            textSize = customAttrs.hoursTextSize
            style = Paint.Style.STROKE
            typeface = Typeface.DEFAULT
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    fun setNotchesVisible(value: Boolean) {
        notchesVisible = value
        invalidate()
    }

    fun setSecondsHandVisible(value: Boolean) {
        secondsHandVisible = value
        invalidate()
    }

    fun setMinutesHandVisible(value: Boolean) {
        minutesHandVisible = value
        invalidate()
    }

    fun setHoursHandVisible(value: Boolean) {
        hoursHandVisible = value
        invalidate()
    }

    fun setHoursTextVisible(value: Boolean) {
        hoursTextVisible = value
        invalidate()
    }

    fun setNotchColor(@ColorInt value: Int) {
        notchesPaint.color = value
        invalidate()
    }

    fun setSecondHandColor(@ColorInt value: Int) {
        secondsHandPaint.color = value
        invalidate()
    }

    fun setMinuteHandColor(@ColorInt value: Int) {
        minutesHandPaint.color = value
        invalidate()
    }

    fun setHourHandColor(@ColorInt value: Int) {
        hoursHandPaint.color = value
        invalidate()
    }

    fun setHourTextColor(@ColorInt value: Int) {
        hoursTextPaint.color = value
        invalidate()
    }

    fun setHoursTextSize(value: Float) {
        hoursTextPaint.textSize = value
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = min(w, h) / 2f - padding
        centerX = w / 2f
        centerY = w / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawNotches(canvas)
        drawHoursText(canvas)
    }

    private fun drawNotches(canvas: Canvas) {
        if (!customAttrs.notchesVisible)
            return
        for (iSecond in 0 until 60) {
            val notchLength = when {
                iSecond % 5 == 0 -> MINUTE_NOTCH_LENGTH_PERC
                else -> SECOND_NOTCH_LENGTH_PERC
            } * radius

            val startX = centerX + (cos(NOTCH_ANGLES[iSecond]) * radius).toFloat()
            val startY = centerY + (sin(NOTCH_ANGLES[iSecond]) * radius).toFloat()
            val stopX = centerX + (cos(NOTCH_ANGLES[iSecond]) * (radius - notchLength)).toFloat()
            val stopY = centerY + (sin(NOTCH_ANGLES[iSecond]) * (radius - notchLength)).toFloat()

            canvas.drawLine(startX, startY, stopX, stopY, notchesPaint)
        }
    }

    private fun drawHoursText(canvas: Canvas) {
        if (!customAttrs.hoursTextVisible)
            return
        val textHeight = hoursTextPaint.fontMetrics.let { it.descent - it.ascent }

        for (iHour in 1..12) {
            val angle = PI / 6 * (iHour - 3)
            val x = centerX + (cos(angle)
                    * (radius - textHeight * 3f))
                    .toFloat()
            val y = centerY + (sin(angle)
                    * (radius - textHeight * 3f))
                    .toFloat()
            canvas.drawText(
                iHour.toString(),
                x,
                y - hoursTextPaint.fontMetrics.descent,
                hoursTextPaint
            )
        }
    }

    private fun drawSecondsHand(canvas: Canvas) {
        if (!customAttrs.secondsHandVisible)
            return

    }

    private fun drawMinutesHand(canvas: Canvas) {
        if (!customAttrs.minutesHandVisible)
            return

    }

    private fun drawHoursHand(canvas: Canvas) {
        if (!customAttrs.hoursHandVisible)
            return
    }
}