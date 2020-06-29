package com.hpcang.base.common.extensions

import android.view.Gravity
import android.widget.Toast
import com.hpcang.base.common.BaseApplication

var text :Int = 0

/**
 * 短时间显示Toast
 */
@JvmOverloads
fun showToast(gravity: Int = Gravity.CENTER, duration: Int = Toast.LENGTH_SHORT, message: () -> String?) {
    val toast = Toast.makeText(BaseApplication.getContext(), message(), duration)
    toast.setGravity(gravity, 0, 0)
    toast.show()
}

/**
 * 长时间显示Toast
 */
inline fun showToastLong(
    gravity: Int = Gravity.CENTER, duration: Int = Toast.LENGTH_LONG, message: () -> String?
) {
    val toast = Toast.makeText(BaseApplication.getContext(), message(), duration)
    toast.setGravity(gravity, 0, 0)
    toast.show()
}