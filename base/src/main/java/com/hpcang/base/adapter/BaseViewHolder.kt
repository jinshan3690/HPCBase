package com.hpcang.base.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var binding: ViewDataBinding

    constructor(binding: ViewDataBinding) : this(binding.root) {
        this.binding = binding
    }

}