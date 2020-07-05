package com.hpcang.base.extensions

import android.util.TypedValue
import android.view.View

/**
 * dp转px
 */
fun View.dp2px(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    ).toInt()
}

/**
 * sp转px
 */
fun View.sp2px(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics
    ).toInt()
}

/**
 * px转dp
 */
fun View.px2dp(value: Float): Float {
    val scale = resources.displayMetrics.density
    return value / scale
}

/**
 * px转sp
 */
fun View.px2sp(value: Float): Float {
    val scale = resources.displayMetrics.scaledDensity
    return value / scale
}
