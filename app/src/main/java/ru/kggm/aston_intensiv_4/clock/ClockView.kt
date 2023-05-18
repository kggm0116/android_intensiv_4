package ru.kggm.aston_intensiv_4.clock

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kggm.aston_intensiv_4.utility.dip2px
import java.time.LocalTime
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
        private const val SECONDS_NOTCH_LENGTH_PERC = 0.08f
        private const val MINUTES_NOTCH_LENGTH_PERC = 0.16f

        private const val HOURS_TEXT_OFFSET_PERC = 0.1f

        private const val HAND_CENTER_OFFSET_PERC = 0.05f

        private const val SECONDS_HAND_LENGTH_PERC = 0.8f
        private const val SECONDS_HAND_WIDTH_DP = 1

        private const val MINUTES_HAND_LENGTH_PERC = 0.55f
        private const val MINUTES_HAND_WIDTH_DP = 4

        private const val HOURS_HAND_LENGTH_PERC = 0.4f
        private const val HOURS_HAND_WIDTH_DP = 8

        private const val NOTCH_WIDTH_DP = 2

        private val ANGLES by lazy { (0 until 60).map { 2 * PI / 60 * it - PI / 2 } }
    }

    private val customAttrs = ClockViewAttrs(context, attrs, defStyleAttr)

    private val padding by lazy { dip2px(PADDING_DP) }

    private val notchWidth by lazy { dip2px(NOTCH_WIDTH_DP) }
    private val secondsHandWidth by lazy { dip2px(HOURS_HAND_WIDTH_DP) }
    private val minutesHandWidth by lazy { dip2px(MINUTES_HAND_WIDTH_DP) }
    private val hoursHandWidth by lazy { dip2px(HOURS_HAND_WIDTH_DP) }

    private val notchesPaint by lazy {
        Paint().apply {
            color = notchColor
            style = Paint.Style.STROKE
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val secondsHandPaint by lazy {
        Paint().apply {
            color = secondsHandColor
            style = Paint.Style.FILL
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val minutesHandPaint by lazy {
        Paint().apply {
            color = minutesHandColor
            style = Paint.Style.FILL
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val hoursHandPaint by lazy {
        Paint().apply {
            color = hoursHandColor
            style = Paint.Style.FILL
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private val hoursTextPaint by lazy {
        Paint().apply {
            color = hoursTextColor
            textSize = hoursTextSize
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL_AND_STROKE
            typeface = Typeface.DEFAULT
            strokeWidth = dip2px(FRAME_WIDTH_DP)
            isAntiAlias = true
        }
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    private var currentSecond = LocalTime.now().second
        set(value) {
            if (field != value) {
                field = value
                Log.i("CLOCK", "Second: $value")
                invalidate()
            }
        }
    private var currentMinute = LocalTime.now().minute
        set(value) {
            if (field != value) {
                field = value
                Log.i("CLOCK", "Minute: $value")
                invalidate()
            }
        }
    private var currentHourBase12 = (LocalTime.now().hour % 12)
        set(value) {
            val base12hour = (value % 12).coerceIn(0..11)
            if (field != base12hour) {
                field = base12hour
                Log.i("CLOCK", "Hour: $base12hour")
                invalidate()
            }
        }

    init {
        startSimpleAnimator()
    }

    var notchesVisible = customAttrs.notchesVisible
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var secondsHandVisible = customAttrs.secondsHandVisible
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var minutesHandVisible = customAttrs.minutesHandVisible
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var hoursHandVisible = customAttrs.hoursHandVisible
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var hoursTextVisible = customAttrs.hoursTextVisible
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var notchColor = customAttrs.notchColor
        set(@ColorInt value) {
            if (field != value) {
                field = value
                notchesPaint.color = value
                invalidate()
            }
        }

    var secondsHandColor = customAttrs.secondsHandColor
        set(@ColorInt value) {
            if (field != value) {
                field = value
                secondsHandPaint.color = value
                invalidate()
            }
        }

    var hoursHandColor = customAttrs.hoursHandColor
        set(@ColorInt value) {
            if (field != value) {
                field = value
                hoursHandPaint.color = value
                invalidate()
            }
        }

    var minutesHandColor = customAttrs.minutesHandColor
        set(@ColorInt value) {
            if (field != value) {
                field = value
                minutesHandPaint.color = value
                invalidate()
            }
        }

    var hoursTextColor = customAttrs.hoursTextColor
        set(@ColorInt value) {
            if (field != value) {
                field = value
                hoursTextPaint.color = value
                invalidate()
            }
        }

    var hoursTextSize = customAttrs.hoursTextSize
        set(value) {
            if (field != value) {
                field = value
                hoursTextPaint.textSize = value
                invalidate()
            }
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
        drawHoursHand(canvas)
        drawMinutesHand(canvas)
        drawSecondsHand(canvas)
    }

    private fun drawNotches(canvas: Canvas) {
        if (!customAttrs.notchesVisible)
            return
        for (iSecond in 0 until 60) {
            val notchLength = when {
                iSecond % 5 == 0 -> MINUTES_NOTCH_LENGTH_PERC
                else -> SECONDS_NOTCH_LENGTH_PERC
            } * radius

            val startX = centerX + (cos(ANGLES[iSecond]) * radius).toFloat()
            val startY = centerY + (sin(ANGLES[iSecond]) * radius).toFloat()
            val stopX = centerX + (cos(ANGLES[iSecond]) * (radius - notchLength)).toFloat()
            val stopY = centerY + (sin(ANGLES[iSecond]) * (radius - notchLength)).toFloat()

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
                    * (radius * (1 - HOURS_TEXT_OFFSET_PERC) - textHeight))
                    .toFloat()
            val y = centerY + (sin(angle)
                    * (radius * (1 - HOURS_TEXT_OFFSET_PERC) - textHeight))
                    .toFloat()
            canvas.drawText(
                iHour.toString(),
                x,
                y + hoursTextPaint.fontMetrics.descent,
                hoursTextPaint
            )
        }
    }

    private fun drawSecondsHand(canvas: Canvas) {
        if (!customAttrs.secondsHandVisible)
            return
        val angle = ANGLES[currentSecond]
        val startX = centerX + (cos(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val startY = centerY + (sin(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val stopX = centerX + (cos(angle) * (radius * SECONDS_HAND_LENGTH_PERC)).toFloat()
        val stopY = centerY + (sin(angle) * (radius * SECONDS_HAND_LENGTH_PERC + secondsHandWidth)).toFloat()
        canvas.drawLine(startX, startY, stopX, stopY, secondsHandPaint)
    }

    private fun drawMinutesHand(canvas: Canvas) {
        if (!customAttrs.minutesHandVisible)
            return
        val angle = ANGLES[currentMinute]
        val startX = centerX + (cos(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val startY = centerY + (sin(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val stopX = centerX + (cos(angle) * (radius * MINUTES_HAND_LENGTH_PERC)).toFloat()
        val stopY = centerY + (sin(angle) * (radius * MINUTES_HAND_LENGTH_PERC + minutesHandWidth)).toFloat()
        canvas.drawLine(startX, startY, stopX, stopY, minutesHandPaint)
    }

    private fun drawHoursHand(canvas: Canvas) {
        if (!customAttrs.hoursHandVisible)
            return
        val angle = ANGLES[currentHourBase12 % 12 * 5]
        val startX = centerX + (cos(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val startY = centerY + (sin(angle) * radius * HAND_CENTER_OFFSET_PERC).toFloat()
        val stopX = centerX + (cos(angle) * (radius * HOURS_HAND_LENGTH_PERC)).toFloat()
        val stopY = centerY + (sin(angle) * (radius * HOURS_HAND_LENGTH_PERC + hoursHandWidth)).toFloat()
        canvas.drawLine(startX, startY, stopX, stopY, hoursHandPaint)
    }

    private var running = true
    private fun startSimpleAnimator() {
        CoroutineScope(Dispatchers.Main).launch {
            while (running) {
                delay(1000)
                onNextSecond()
            }
        }
    }

    private fun onNextSecond() {
        if (currentSecond == 59) {
            onNextMinute()
            currentSecond = 0
        }
        else {
            currentSecond++
        }
    }

    private fun onNextMinute() {
        if (currentMinute == 59) {
            onNextHour()
            currentMinute = 0
        }
        else {
            currentMinute++
        }
    }

    private fun onNextHour() {
        if (currentHourBase12 == 11)
            currentHourBase12 = 0
        else
            currentHourBase12++
    }
}