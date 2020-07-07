package com.hpcang.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hpcang.R
import com.hpcang.base.BaseActivity
import com.hpcang.base.adapter.*
import com.hpcang.databinding.ItemSimpleABinding
import com.hpcang.databinding.ItemSimpleBBinding
import com.hpcang.databinding.ItemSimpleCBinding

class SimpleAdapter3(context: Context) :
    ListBindingAdapter<String, ItemSimpleCBinding>(context, R.layout.item_simple_c) {

    override fun getView(binding: ItemSimpleCBinding?, position: Int, item: String): View {
        binding?.apply {
            data = item
        }
        return binding!!.root
    }


}