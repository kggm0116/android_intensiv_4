package ru.kggm.aston_intensiv_4.utility

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

sealed class ColorResource {
    @ColorInt abstract fun getColor(context: Context): Int
    data class ColorRes(@androidx.annotation.ColorRes val color: Int): ColorResource() {
        override fun getColor(context: Context): Int {
            return ContextCompat.getColor(context, color)
        }
    }
}
