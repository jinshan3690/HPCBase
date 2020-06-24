package com.hpcang.base.common.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.hpcang.base.common.BaseActivity
import com.hpcang.base.common.R
import com.hpcang.base.common.extensions.showToast
import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * Created by Js on 2016/6/29.
 */
class AcManager(private val context: Activity) {

    private var view: View? = null
    private var color = R.color.statusBarColor
    private var statusBarVisibility = true

    /**
     * onCreate之前
     * 沉浸式
     */
    var isStatusTrans = false
    private var mTintManager: SystemBarTintManager? = null
    private var isDoubleExit = false


    init {
        if (context is BaseActivity) view = context.view
    }

    /**
     * 改变状态栏颜色
     */
    fun changeStatusColor(v: View, color: Int) {
        setStateBarColor(color)
        changeStatusBar(v)
    }

    /**
     * 改变状态栏颜色
     */
    fun changeStatusBar(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mTintManager == null) {
                setTranslucentStatus(v)
            } else {
                mTintManager?.setStatusBarTintResource(color)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setTranslucentStatus(v: View) {
        val window = context.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        var stateHeight = statusHeight
        if (!isStatusTrans) {
            mTintManager = SystemBarTintManager(context)
            mTintManager?.isStatusBarTintEnabled = true
            mTintManager?.setStatusBarTintResource(color)
            val actionHeight = actionHeight
            if (!statusBarVisibility) stateHeight = 0
            v.setPadding(0, stateHeight + actionHeight, 0, 0)
        } else {
            v.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * 获得状态栏的高度
     */
    val statusHeight: Int
        get() {
            return SystemUtil.getStatusHeight(context)
        }

    /**
     * 获得ActionBar的高度
     */
    val actionHeight: Int
        get() {
            var actionBarHeight = 0
            if (context is AppCompatActivity) {
                val compatActivity = context
                compatActivity.supportActionBar?.run {
                    if(isShowing) {
                        val tv = TypedValue()
                        if (
                            context.getTheme().resolveAttribute(
                                android.R.attr.actionBarSize, tv, true)
                        ) {
                            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                                tv.data, context.getResources().displayMetrics
                            )
                        }
                    }
                }
            }
            return actionBarHeight
        }

    /**
     * 显示隐藏状态栏
     */
    fun setStatusBarVisibility(context: Activity, enable: Boolean) {
        statusBarVisibility = enable
        val window = context.window
        if (!enable) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            mTintManager = SystemBarTintManager(context)
            mTintManager?.isStatusBarTintEnabled = true
            mTintManager?.setStatusBarTintResource(Color.TRANSPARENT)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
            mTintManager = SystemBarTintManager(context)
            mTintManager?.isStatusBarTintEnabled = true
            mTintManager?.setStatusBarTintResource(color)
        }
        val actionHeight = actionHeight
        var stateHeight = statusHeight
        if (!enable) stateHeight = 0
        view?.setPadding(0, stateHeight + actionHeight, 0, 0)
    }

    fun setStatusLight() {
        val window = context.window
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    fun setStatusDark() {
        val window = context.window
        window.decorView.systemUiVisibility = window.decorView
            .systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    /**
     * 设置是否全屏
     */
    fun toggleFullScreen(enable: Boolean) {
        val window = context.window
        val lp = window.attributes
        if (enable) {
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }
        window.attributes = lp
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**
     * 显示Navigation并且全屏
     */
    fun toggleNavigation(v: View, show: Boolean) {
        if (show) {
            val uiOptions =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
            v.systemUiVisibility = uiOptions
        } else {
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            v.systemUiVisibility = uiOptions
        }
    }

    /**
     * 设置是否横屏
     */
    fun setOrientation(enable: Boolean) {
        if (enable) context.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else context.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * onCreate之后
     * 状态栏颜色
     */
    fun setStateBarColor(color: Int) {
        this.color = color
    }

    /**
     * 点击屏幕隐藏软键盘
     */
    fun hideSoftInput(event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_DOWN == event?.action && null != context.currentFocus) {
            val mInputMethodManager =
                context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(
                context.currentFocus?.windowToken, 0
            )
        }
        return false
    }

    /**
     * 双击退出
     */
    private var isExit = false
    fun doubleExit(keyCode: Int): Boolean {
        if (isDoubleExit && !isExit && keyCode == KeyEvent.KEYCODE_BACK) {
            showToast { "再按一次退出程序" }
            isExit = true
            Handler().postDelayed({ isExit = false }, 3000)
            return true
        }
        return false
    }

    /**
     * 开始双击退出
     */
    fun setDoubleExit(isDoubleExit: Boolean) {
        this.isDoubleExit = isDoubleExit
    }

    companion object {

        fun getInstance(context: Activity): AcManager {
            return AcManager(context)
        }

        /**
         * 跳转方法
         */
        fun toActivity(context: Context, url: String) {
            ARouter.getInstance().build(url).navigation(context)
        }

        fun toActivity(
            context: Context, url: String, compat: ActivityOptionsCompat
        ) {
            ARouter.getInstance()
                .build(url)
                .withOptionsCompat(compat).navigation(context)
        }

        fun toActivityForResult(
            context: Activity, url: String, request: Int
        ) {
            ARouter.getInstance().build(url).navigation(context, request)
        }

        fun toActivityForResult(
            context: Activity, url: String, compat: ActivityOptionsCompat, request: Int
        ) {
            ARouter.getInstance()
                .build(url)
                .withOptionsCompat(compat).navigation(context, request)
        }

        fun toActivityForData(url: String): Postcard {
            return ARouter.getInstance().build(url)
        }

        fun toActivityForData(url: String, compat: ActivityOptionsCompat): Postcard {
            return ARouter.getInstance()
                .build(url)
                .withOptionsCompat(compat)
        }

        fun getCompat(
            context: Activity, v: View, target: String
        ): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(
                context, v, target
            )
        }
    }
}