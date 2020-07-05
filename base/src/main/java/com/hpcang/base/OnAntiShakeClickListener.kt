package com.hpcang.base

import android.view.View
import com.hpcang.base.util.AntiShakeUtils

abstract class OnAntiShakeClickListener : View.OnClickListener {

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return
        antiShakeOnClick(v)
    }

    abstract fun antiShakeOnClick(v: View?)

}