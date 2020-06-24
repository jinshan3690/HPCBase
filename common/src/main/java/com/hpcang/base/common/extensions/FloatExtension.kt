package com.hpcang.base.common.extensions

import android.util.TypedValue
import com.hpcang.base.common.BaseApplication

/**
 * dp转px
 */
fun Float.dp2px(): Int {
    return this
        .takeIf { BaseApplication.context != null }
        ?.run {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this, BaseApplication.context!!.resources.displayMetrics
            ).toInt()
        }
        .run { 0 }
}

/**
 * sp转px
 */
fun Float.sp2px(): Int {
  return this
    .takeIf { BaseApplication.context != null }
    ?.run {
      TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, BaseApplication.context!!.resources.displayMetrics
      ).toInt()
    }
    .run { 0 }
}

/**
 * px转dp
 */
fun Float.px2dp(): Float {
  return this
    .takeIf { BaseApplication.context != null }
    ?.run {
      val scale = BaseApplication.context!!.resources.displayMetrics.density
      return this / scale
    }
    .run { 0f }
}

/**
 * px转sp
 */
fun Float.px2sp(): Float {
    return this
    .takeIf { BaseApplication.context != null }
    ?.run {
      val scale = BaseApplication.context!!.resources.displayMetrics.scaledDensity
      return this / scale
    }
    .run { 0f }
}
