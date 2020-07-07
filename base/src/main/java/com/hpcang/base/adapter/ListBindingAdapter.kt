package com.hpcang.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hpcang.base.BaseActivity
import com.hpcang.base.OnAntiShakeClickListener
import java.lang.RuntimeException
import java.util.*

/**
 * Created by Js on 2016/5/13.
 */
abstract class ListBindingAdapter<T, B : ViewDataBinding> : BaseAdapter {
    protected var context: Context? = null
    var data: MutableList<T> = ArrayList()
    fun set(data: List<T>?) {
        if (data != null) {
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

    private var layoutId: Int
    protected var itemClickListener: OnItemClickListener? = null

    constructor(
        context: Context?, layoutId: Int = 0, data: List<T>? = null
    ) {
        this.context = context
        if (data != null) this.data.addAll(data)
        this.layoutId = layoutId
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): T {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int, contentView: View?, parent: ViewGroup
    ): View {
        if (data.size == 0)
            throw RuntimeException("data.size不能为0")
        return when {
            layoutId == 0 -> {
                getView(null, position, data[position])
            }
            contentView == null -> {
                val binding = binding(layoutId, parent)
                binding.root.tag = binding
                val itemView = getView(
                    binding, position, data[position]
                )
                binding.executePendingBindings()
                return itemView
            }
            else -> {
                val binding = contentView.tag as B
                val itemView = getView(binding, position, data[position])
                binding.executePendingBindings()
                return itemView
            }
        }
    }

    abstract fun getView(binding: B?, position: Int, item: T):View

    fun clearData() {
        data.clear()
    }

    open fun binding(layoutId: Int, parent: ViewGroup, attachToParent: Boolean = false): B {
        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(context), layoutId, parent, attachToParent
        )

        binding.apply {
            lifecycleOwner = context as BaseActivity
        }
        return binding
    }

    interface OnItemClickListener {
        fun itemClick(v: View?, position: Int)
        fun itemLongClick(v: View?, position: Int)
    }

    fun setOnClickListener(view: View, position: Int) {
        setOnClickListener(view, position, true)
    }

    fun setOnClickListener(
        view: View, position: Int, hasAntiShake: Boolean
    ) {
        if (hasAntiShake) {
            view.setOnClickListener(object : OnAntiShakeClickListener() {
                override fun antiShakeOnClick(v: View?) {
                    if (itemClickListener != null) itemClickListener!!.itemClick(view, position)
                }
            })
        } else {
            view.setOnClickListener { v: View? ->
                if (itemClickListener != null) itemClickListener!!.itemClick(
                    view, position
                )
            }
        }
    }

    fun setOnLongClickListener(view: View, position: Int) {
        view.setOnLongClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.itemLongClick(view, position)
                return@setOnLongClickListener true
            }
            false
        }
    }
}