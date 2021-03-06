package com.hpcang.base.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hpcang.base.OnAntiShakeClickListener
import java.lang.RuntimeException
import java.util.*

abstract class BaseRecyclerAdapter<Y : BaseViewHolder?, T : Any?> :
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
        var view = View(context)
        if (layoutId != 0) view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return onCreateViewHolder(parent, view, viewType)
    }

    protected open fun onCreateViewHolder(parent: ViewGroup, view: View, viewType: Int): Y {
        return BaseViewHolder(view) as Y
    }

    override fun onBindViewHolder(holder: Y, position: Int) {
        if (currentList.size == 0)
            throw RuntimeException("data.size不能为0")
        onBindViewHolder(holder, position, currentList[position])
    }

    abstract fun onBindViewHolder(holder: Y, position: Int, item: T)

    fun inflate(layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null)
    }

    fun inflate(layout: Int, view: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(layout, view, false)
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