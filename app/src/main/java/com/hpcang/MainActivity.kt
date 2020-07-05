package com.hpcang

import android.os.Bundle
import androidx.activity.viewModels
import com.hpcang.base.BaseActivity
import com.hpcang.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val viewModel by viewModels<AAA>()

    override fun onCreateBefore(savedInstanceState: Bundle?) {
        super.onCreateBefore(savedInstanceState)
    }

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