package com.hpcang.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.hpcang.base.common.BaseActivity
import com.hpcang.base.common.extensions.showToast
import com.hpcang.base.common.extensions.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

//@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        }

    override fun initLoading() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun initView() {

    }

    override fun queryData() {

    }
}