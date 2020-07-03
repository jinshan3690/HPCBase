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
import com.hpcang.base.common.util.AcStack

abstract class BaseFragment : Fragment() {

    open val mContext: BaseActivity by lazy {  activity as BaseActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        initViewBefore()
        return initBinding(container)
    }

    inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int, container:ViewGroup?
    ): T = DataBindingUtil.inflate<T>(layoutInflater, resId, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        queryData()
    }

    open fun onCreateBefore(savedInstanceState: Bundle?) {}

    open fun initViewBefore() {}

    abstract fun initBinding(container: ViewGroup?):View?

    abstract fun initView()

    abstract fun queryData()

    open fun showLoading() {
        mContext.showLoading()
    }

    open fun hideLoading() {
        mContext.hideLoading()
    }

    /**
     * 跳转方法
     */
    fun toActivity(url: String) {
        AcManager.toActivity(mContext, url)
    }

    fun toActivity(url: String, compat: ActivityOptionsCompat) {
        AcManager.toActivity(mContext, url, compat)
    }

    fun toActivityForResult(url: String, request: Int) {
        AcManager.toActivityForResult(mContext, url, request)
    }

    fun toActivityForResult(
        url: String, compat: ActivityOptionsCompat, request: Int
    ) {
        AcManager.toActivityForResult(mContext, url, compat, request)
    }

    fun toActivityForData(url: String): Postcard {
        return AcManager.toActivityForData(url)
    }

    fun toActivityForData(url: String, compat: ActivityOptionsCompat): Postcard {
        return AcManager.toActivityForData(url, compat)
    }

    fun getCompat(v: View, target: String): Bundle? {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, v, target)
        return options.toBundle()
    }

}