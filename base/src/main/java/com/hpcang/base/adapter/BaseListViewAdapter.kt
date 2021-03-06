package com.hpcang.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hpcang.base.OnAntiShakeClickListener
import java.lang.RuntimeException
import java.util.*

/**
 * Created by Js on 2016/5/13.
 */
abstract class BaseListViewAdapter<T> : BaseAdapter {

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
        position: Int, contentView: View?, parent: ViewGroup?
    ): View? {
        var contentView = contentView
        if (contentView == null) {
            contentView = if (layoutId != 0) inflate(layoutId) else View(context)
        }
        if (data.size == 0)
            throw RuntimeException("data.size不能为0")
        getView(position, contentView!!, parent, data[position])
        return contentView
    }

    protected abstract fun getView(
        position: Int, contentView: View, parent: ViewGroup?, item: T
    )

    fun clearData() {
        data.clear()
    }

    fun inflate(layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null)
    }

    fun inflate(layout: Int, view: ViewGroup): View {
        return LayoutInflater.from(context).inflate(layout, view, false)
    }

    interface OnItemClickListener {
        fun itemClick(v: View, position: Int)
        fun itemLongClick(v: View, position: Int)
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