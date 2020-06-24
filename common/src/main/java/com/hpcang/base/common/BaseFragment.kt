package com.hpcang.base.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.hpcang.base.common.util.AcManager

abstract class BaseFragment : Fragment() {

    lateinit var context: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as BaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initView(container, savedInstanceState)
    }

    inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int, container:ViewGroup?
    ): Lazy<T> = lazy {
        DataBindingUtil.inflate<T>(layoutInflater, resId, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

    }

    abstract fun initView(container: ViewGroup?, savedInstanceState: Bundle?):View?

    abstract fun initData()

    open fun showLoading() {
        context.showLoading()
    }

    open fun hideLoading() {
        context.hideLoading()
    }

    /**
     * 跳转方法
     */
    fun toActivity(url: String) {
        AcManager.toActivity(context, url)
    }

    fun toActivity(url: String, compat: ActivityOptionsCompat) {
        AcManager.toActivity(context, url, compat)
    }

    fun toActivityForResult(url: String, request: Int) {
        AcManager.toActivityForResult(context, url, request)
    }

    fun toActivityForResult(
        url: String, compat: ActivityOptionsCompat, request: Int
    ) {
        AcManager.toActivityForResult(context, url, compat, request)
    }

    fun toActivityForData(url: String): Postcard {
        return AcManager.toActivityForData(url)
    }

    fun toActivityForData(url: String, compat: ActivityOptionsCompat): Postcard {
        return AcManager.toActivityForData(url, compat)
    }

    fun getCompat(v: View, target: String): Bundle? {
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(context, v, target)
        return options.toBundle()
    }

}