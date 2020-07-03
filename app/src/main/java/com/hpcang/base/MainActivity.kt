package com.hpcang.base

import com.hpcang.base.common.BaseActivity
import com.hpcang.base.common.extensions.showToast
import com.hpcang.base.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)


    override fun initLoading() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@MainActivity
        }
    }

    override fun initView() {

    }

    override fun queryData() {

    }
}