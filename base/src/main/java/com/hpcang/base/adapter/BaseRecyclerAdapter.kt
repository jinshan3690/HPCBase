package com.hpcang.base.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.hpcang.base.OnAntiShakeClickListener
import java.util.*

abstract class BaseRecyclerAdapter<Y : RecyclerView.ViewHolder?, T : Any?> : RecyclerView.Adapter<Y> {

    var lastPosition = -1
    var currentPosition = 0
    var data: MutableList<T> = ArrayList()
    fun set(data: List<T>?) {
        if (data != null) {
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }
    protected var context: Context
    protected var layoutId: Int? = null
    var itemClickListener: OnItemClickListener? = null

    constructor(context: Context, data: List<T>? = null, layoutId: Int? = null) {
        if (data != null) this.data.addAll(data)
        this.context = context
        this.layoutId = layoutId
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Y {
        var view: View = View(context)
        if (layoutId != null) view = LayoutInflater.from(context).inflate(layoutId!!, parent, false)
        return onCreateViewHolder(parent, view, viewType)
    }

    protected fun onCreateViewHolder(
        parent: ViewGroup, view: View, viewType: Int
    ): Y {
        return BaseViewHolder(view) as Y
    }

    override fun onBindViewHolder(holder: Y, position: Int) {
        currentPosition = position
        onBindViewHolder(holder, position, if (data.size == 0) Any() as T else data[position])
    }

    abstract fun onBindViewHolder(holder: Y, position: Int, item: T)

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    interface OnItemClickListener {
        fun itemClick(v: View?, position: Int)
        fun itemLongClick(v: View?, position: Int)
    }

    protected fun setOnClickListener(view: View, position: Int) {
        setOnClickListener(view, position, true)
    }

    private fun setOnClickListener(
        view: View, position: Int, hasAntiShake: Boolean
    ) {
        if (hasAntiShake) {
            view.setOnClickListener(object : OnAntiShakeClickListener() {
                override fun antiShakeOnClick(v: View?) {
                    if (itemClickListener != null) itemClickListener!!.itemClick(view, position)
                }
            })
        } else {
            view.setOnClickListener {
                if (itemClickListener != null) itemClickListener!!.itemClick(
                    view, position
                )
            }
        }
    }

    protected fun setOnLongClickListener(view: View, position: Int) {
        view.setOnLongClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.itemLongClick(view, position)
                return@setOnLongClickListener true
            }
            false
        }
    }

    fun inflate(layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null)
    }

    fun inflate(layout: Int, view: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(layout, view, false)
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    fun addData(data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size)
    }

    fun addDataAll(data: List<T>) {
        val start = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(start, data.size)
    }

    fun removeData(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAllData() {
        notifyItemRangeRemoved(0, data!!.size)
    }

    fun clearData() {
        lastPosition = -1
        data.clear()
    }

    protected fun startAnimators(holder: RecyclerView.ViewHolder) {
        val adapterPosition = holder.adapterPosition
        if (adapterPosition > lastPosition) {
            for (anim in getAnimators(holder.itemView)) {
                anim.interpolator = LinearInterpolator()
                anim.setDuration(300).start()
            }
            lastPosition = adapterPosition
        } else {
//            ViewHelper.clear(holder.itemView);
        }
    }

    private fun getAnimators(view: View): List<ObjectAnimator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f)
        return listOf(scaleX, scaleY)
    }
}