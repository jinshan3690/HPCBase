package com.hpcang.base.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hpcang.base.BaseActivity
import com.hpcang.base.OnAntiShakeClickListener
import com.hpcang.base.util.L
import java.util.concurrent.Executor

abstract class RecyclerBindingAdapter<Y : BaseViewHolder?, T : Any?, B : ViewDataBinding> :
    ListAdapter<T, Y> {

    protected var context: Context
    private var layoutId: Int
    var itemClickListener: OnItemClickListener? = null

    constructor(
        context: Context, layoutId: Int = 0,
        diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return false
            }
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return false
            }
        }
    ) : super(
        AsyncDifferConfig.Builder<T>(diffCallback).build()
    ) {
        this.context = context
        this.layoutId = layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Y {
        return onCreateViewHolder(
            parent, if (layoutId != 0) binding(layoutId, parent) else null, viewType
        )
    }

    open fun onCreateViewHolder(
        parent: ViewGroup, binding: B?, viewType: Int
    ): Y {
        if (binding == null)
            throw RuntimeException("请重写onCreateViewHolder或传入layoutId")
        return BaseViewHolder(binding) as Y
    }

    override fun onBindViewHolder(holder: Y, position: Int) {
        if (currentList.size == 0)
            throw RuntimeException("data.size不能为0")
        onBindViewHolder(
            holder, position, getItem(position)
        )
        holder?.binding?.executePendingBindings()
    }

    abstract fun onBindViewHolder(binding: Y, position: Int, item: T)

    open fun binding(layoutId: Int, parent: ViewGroup, attachToParent: Boolean = false): B {
        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(context), layoutId, parent, attachToParent
        )

        binding.apply {
            lifecycleOwner = context as BaseActivity
        }
        return binding
    }

    fun getItemData(position: Int): T {
        return super.getItem(position)
    }

    interface OnItemClickListener {
        fun itemClick(v: View?, position: Int)
        fun itemLongClick(v: View?, position: Int)
    }

    fun setOnClickListener(view: View, viewHolder: RecyclerView.ViewHolder) {
        setOnClickListener(view, viewHolder, true)
    }

    fun setOnClickListener(view: View, viewHolder: RecyclerView.ViewHolder, hasAntiShake: Boolean) {
        if (hasAntiShake) {
            view.setOnClickListener(object : OnAntiShakeClickListener() {
                override fun antiShakeOnClick(v: View?) {
                    if (itemClickListener != null) itemClickListener!!.itemClick(
                        view, viewHolder.adapterPosition
                    )
                }
            })
        } else {
            view.setOnClickListener {
                if (itemClickListener != null) itemClickListener!!.itemClick(
                    view, viewHolder.adapterPosition
                )
            }
        }
    }

    fun setOnLongClickListener(view: View, viewHolder: RecyclerView.ViewHolder) {
        view.setOnLongClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.itemLongClick(view, viewHolder.adapterPosition)
                return@setOnLongClickListener true
            }
            false
        }
    }

}