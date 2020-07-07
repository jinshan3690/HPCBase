package com.hpcang

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.ObservableList
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hpcang.adapter.SimpleAdapter2
import com.hpcang.adapter.SimpleAdapter3
import com.hpcang.base.BaseActivity
import com.hpcang.base.adapter.RecyclerBindingAdapter
import com.hpcang.base.extensions.showToast
import com.hpcang.base.util.L
import com.hpcang.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    /**
     * Init
     * 初始化
     */
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val viewModel by viewModels<MainViewModel>()
    private val adapter2 by lazy { SimpleAdapter2(context) }
    private val adapter3 by lazy { SimpleAdapter3(context) }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun initLoading() {}

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }
        registerBinding(viewModel)
    }


    override fun initView() {
        acManager.toggleNavigation(view!!, false)
        acManager.setDoubleExit(true)

        adapter2.itemClickListener = object: RecyclerBindingAdapter.OnItemClickListener{
            override fun itemClick(v: View?, position: Int) {
                L.e(Gson().toJson(adapter2.getItemData(position)))
            }

            override fun itemLongClick(v: View?, position: Int) {

            }
        }
        recyclerView.adapter = adapter2

        listView.adapter = adapter3

        viewModel.userList.observe(context, Observer {
            Log.e("aaaaaaaaaaa", "UserFragment:userList")
//            val users = mutableListOf<String>()
//            for ( i in 0 until (Math.random()*10).toInt()) {
//                users.add((Math.random()*2).toInt().toString())
//            }
//            adapter3.data = users
            adapter2.submitList(it) {}
        })
        refresh.setOnClickListener {
            viewModel.setUserId()
        }
    }

    /**
     * Event
     * 请求/事件处理
     */
    override fun queryData() {

    }

    /**
     * Result
     * 接口返回
     */

    /**
     * Other
     * 生命周期和辅助方法
     */
    override fun onResume() {
        super.onResume()
        Log.e("aaaaaaaaaaa", "UserFragment:onResume")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("aaaaaaaaaaa", "UserFragment:onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("aaaaaaaaaaa", "UserFragment:onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.e("aaaaaaaaaaa", "UserFragment:onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.e("aaaaaaaaaaa", "UserFragment:onPause")
    }


}