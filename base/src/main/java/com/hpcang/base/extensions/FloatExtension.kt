package com.hpcang.base.extensions

import android.util.TypedValue
import com.hpcang.base.BaseApplication

/**
 * dp转px
 */
fun Float.dp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, BaseApplication.getContext().resources.displayMetrics
    ).toInt()
}

/**
 * sp转px
 */
fun Float.sp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, BaseApplication.getContext().resources.displayMetrics
    ).toInt()
}

/**
 * px转dp
 */
fun Float.px2dp(): Float {
    val scale = BaseApplication.getContext().resources.displayMetrics.density
    return this / scale
}

/**
 * px转sp
 */
fun Float.px2sp(): Float {
    val scale = BaseApplication.getContext().resources.displayMetrics.scaledDensity
    return this / scale
}
