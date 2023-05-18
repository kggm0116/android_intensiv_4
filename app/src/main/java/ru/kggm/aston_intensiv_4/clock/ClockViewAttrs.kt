package ru.kggm.aston_intensiv_4.clock

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import ru.kggm.aston_intensiv_4.R

internal class ClockViewAttrs(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) {
    private val attributesArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ClockView,
        defStyleAttr,
        0
    )

    @ColorInt
    var notchColor = attributesArray.getColor(
        R.styleable.ClockView_notchesColor, Color.BLACK
    )

    @ColorInt
    var hoursTextColor = attributesArray.getColor(
        R.styleable.ClockView_hoursTextColor, Color.BLACK
    )
    var hoursTextSize = attributesArray.getDimension(
        R.styleable.ClockView_hoursTextSize, 32f
    )

    @ColorInt
    var secondsHandColor = attributesArray.getColor(
        R.styleable.ClockView_secondsHandColor, Color.BLACK
    )
    @ColorInt
    var minutesHandColor = attributesArray.getColor(
        R.styleable.ClockView_minutesHandColor, Color.BLACK
    )
    @ColorInt
    var hoursHandColor = attributesArray.getColor(
        R.styleable.ClockView_hoursHandColor, Color.BLACK
    )

    var notchesVisible = attributesArray.getBoolean(
        R.styleable.ClockView_notchesVisible, true
    )
    var hoursTextVisible = attributesArray.getBoolean(
        R.styleable.ClockView_hoursTextVisible, true
    )
    var secondsHandVisible = attributesArray.getBoolean(
        R.styleable.ClockView_secondsHandVisible, true
    )
    var minutesHandVisible = attributesArray.getBoolean(
        R.styleable.ClockView_minutesHandVisible, true
    )
    var hoursHandVisible = attributesArray.getBoolean(
        R.styleable.ClockView_hoursHandVisible, true
    )
}