package com.hpcang.base

import android.view.View
import android.view.ViewGroup
import com.hpcang.base.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :BaseFragment() {

    override fun initBinding(container: ViewGroup?): View? {
        return binding<FragmentMainBinding>(R.layout.fragment_main, container).root
    }

    override fun initView() {

    }

    override fun queryData() {

    }
}