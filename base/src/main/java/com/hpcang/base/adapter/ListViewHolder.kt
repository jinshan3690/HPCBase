package com.hpcang.base.adapter

import android.util.SparseArray
import android.view.View

object ListViewHolder {

    operator fun <T : View?> get(view: View, id: Int): T? {
        var viewHolder =
            view.tag as SparseArray<View>
        if (viewHolder == null) {
            viewHolder = SparseArray()
            view.tag = viewHolder
        }
        var childView = viewHolder[id]
        if (childView == null) {
            childView = view.findViewById(id)
            viewHolder.put(id, childView)
        }
        return childView as T?
    }

}