package com.hpcang.base.common

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alibaba.android.arouter.facade.Postcard
import com.hpcang.base.common.extensions.dp2px
import com.hpcang.base.common.util.AcManager
import com.hpcang.base.common.util.AcStack
import me.jessyan.autosize.AutoSizeCompat

@SuppressLint("RestrictedApi")
abstract class BaseActivity : ComponentActivity() {

    lateinit var context: BaseActivity
    var view: View? = null
    val acManager: AcManager by lazy { AcManager.getInstance(context) }

    var loadingView: ViewGroup? = null
    var loadingProgressView: CircularProgressDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        context = this
        initViewBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        AcStack.create().addActivity(this)
    }

    fun initViewBefore(savedInstanceState: Bundle?) {
        acManager.isStatusTrans = true
        acManager.setStatusDark()
    }

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, resId)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)

        this.view = view
        acManager.changeStatusBar(view)

        initView()
        queryData()
    }

    abstract fun showLoading()

    abstract fun hideLoading()

    abstract fun initView()

    abstract fun queryData()

    /**
     * 屏幕适配
     */
    override fun getResources(): Resources? {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

    /**
     * 点击隐藏键盘
     */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        acManager.hideSoftInput(event)
        return super.dispatchTouchEvent(event)
    }

    /**
     * 双击退出
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (acManager.doubleExit(keyCode)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    /**
     * 跳转方法
     */
    fun toActivity(url: String?) {
        AcManager.toActivity(context, url)
    }

    fun toActivity(url: String?, compat: ActivityOptionsCompat?) {
        AcManager.toActivity(context, url, compat)
    }

    fun toActivityForResult(url: String?, request: Int) {
        AcManager.toActivityForResult(context, url, request)
    }

    fun toActivityForResult(
        url: String?, compat: ActivityOptionsCompat?, request: Int
    ) {
        AcManager.toActivityForResult(context, url, compat, request)
    }

    fun toActivityForData(url: String?): Postcard {
        return AcManager.toActivityForData(url)
    }

    fun toActivityForData(url: String?, compat: ActivityOptionsCompat?): Postcard {
        return AcManager.toActivityForData(url, compat)
    }

    fun getCompat(v: View?, target: String?): Bundle? {
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(context, v!!, target!!)
        return options.toBundle()
    }

}