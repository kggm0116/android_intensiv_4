package ru.kggm.aston_intensiv_4.utility

import android.content.Context
import android.view.View

fun Context.dip2px(value: Int) = resources.displayMetrics.density * value

fun View.dip2px(value: Int) = resources.displayMetrics.density * value