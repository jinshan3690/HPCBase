package com.hpcang.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hpcang.R
import com.hpcang.base.adapter.BaseViewHolder
import com.hpcang.base.adapter.RecyclerBindingAdapter
import com.hpcang.databinding.ItemSimpleABinding
import com.hpcang.databinding.ItemSimpleBBinding

class SimpleAdapter2(context: Context) :
    RecyclerBindingAdapter<BaseViewHolder, String, ViewDataBinding>(context,
        diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }) {

    override fun getItemViewType(position: Int): Int {
        return position.rem(2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, binding: ViewDataBinding?, viewType: Int
    ): BaseViewHolder {
        val viewHolder = if (viewType == 0)
            BaseViewHolder(binding(R.layout.item_simple_a, parent))
        else
            BaseViewHolder(binding(R.layout.item_simple_b, parent))

        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: String) {
        when (val binding = holder.binding) {
            is ItemSimpleABinding -> {
                binding.apply {
                    data = item
                }
            }
            is ItemSimpleBBinding -> {
                binding.apply {
                    data = item
                }
            }
        }
        setOnClickListener(holder.itemView, holder)
    }


}