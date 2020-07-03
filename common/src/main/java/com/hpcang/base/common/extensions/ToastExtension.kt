package com.hpcang.base.common.extensions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.hpcang.base.common.BaseApplication
import com.hpcang.base.common.R

/**
 * 短时间显示Toast
 */
inline fun showToast(
    gravity: Int = Gravity.CENTER, duration: Int = Toast.LENGTH_SHORT,
    view: () -> View? = { null }, message: () -> String?
) {
    val toast = Toast(BaseApplication.getContext())
    toast.view = getToastView(view, message)
    toast.setGravity(gravity, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

/**
 * 长时间显示Toast
 */
inline fun showToastLong(
    gravity: Int = Gravity.CENTER, duration: Int = Toast.LENGTH_LONG,
    view: () -> View? = { null }, message: () -> String?
) {
    val toast = Toast(BaseApplication.getContext())
    toast.view = getToastView(view, message)
    toast.setGravity(gravity, 0, 0)
    toast.duration = Toast.LENGTH_LONG
    toast.show()
}

inline fun getToastView(root: () -> View?, message: () -> String?): View {
    var view = root()
    if (view == null) {
        view = FrameLayout(BaseApplication.getContext())
        view.setBackgroundResource(R.drawable.toast_view)
        val textView = TextView(BaseApplication.getContext())
        textView.setTextColor(Color.WHITE)
        textView.text = message()
        textView.textSize = 14f
        textView.setPadding(10f.dp2px(), 8f.dp2px(), 10f.dp2px(), 8f.dp2px())
        (view as ViewGroup).addView(textView)
    }
    return view
}