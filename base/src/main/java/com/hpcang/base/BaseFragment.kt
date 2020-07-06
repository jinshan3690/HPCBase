package com.hpcang.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.hpcang.base.util.AcManager

abstract class BaseFragment : Fragment() {

    val baseActivity: BaseActivity by lazy {  activity as BaseActivity }

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

    protected open fun registerBinding(binding : ViewDataBinding,viewModel : BaseViewModel): View? {
        viewModel.finishLiveData.observe(this, Observer { baseActivity.finish() })
        viewModel.showLoadingLiveData.observe(this, Observer{ showLoading() })
        viewModel.hideLoadingLiveData.observe(this, Observer{ hideLoading() })
        viewModel.httpErrorLiveData.observe(this, Observer { params ->
            httpError(
                params[BaseViewModel.ParameterField.MESSAGE] as String?,
                params[BaseViewModel.ParameterField.TYPE] as Int,
                params[BaseViewModel.ParameterField.DATA]
            )
        })
        return binding.root
    }

    open fun httpError(message:String?, type:Int, data:Any?){}

    open fun showLoading() {
        baseActivity.showLoading()
    }

    open fun hideLoading() {
        baseActivity.hideLoading()
    }

    /**
     * 跳转方法
     */
    fun toActivity(url: String) {
        AcManager.toActivity(baseActivity, url)
    }

    fun toActivity(url: String, compat: ActivityOptionsCompat) {
        AcManager.toActivity(baseActivity, url, compat)
    }

    fun toActivityForResult(url: String, request: Int) {
        AcManager.toActivityForResult(baseActivity, url, request)
    }

    fun toActivityForResult(
        url: String, compat: ActivityOptionsCompat, request: Int
    ) {
        AcManager.toActivityForResult(baseActivity, url, compat, request)
    }

    fun toActivityForData(url: String): Postcard {
        return AcManager.toActivityForData(url)
    }

    fun toActivityForData(url: String, compat: ActivityOptionsCompat): Postcard {
        return AcManager.toActivityForData(url, compat)
    }

    fun getCompat(v: View, target: String): Bundle? {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(baseActivity, v, target)
        return options.toBundle()
    }

}