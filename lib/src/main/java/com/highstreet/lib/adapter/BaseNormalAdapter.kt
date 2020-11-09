package com.highstreet.lib.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.highstreet.lib.view.listener.RxView

abstract class BaseNormalAdapter<T>(
        private val layoutId: Int,
        var data: ArrayList<T> = ArrayList()
) : RecyclerView.Adapter<ViewHolder>() {

    lateinit var context: Context

    var itemClickListener: OnItemClickListener<T>? = null

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(context, parent, layoutId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemClickListener?.let {
            RxView.click(holder.itemView) {
                it.itemClicked(holder.itemView, getItem(position)!!, position)
            }
        }
        bindViewHolder(holder, getItem(position), position)
    }

    protected fun getItem(position: Int) = data[position]

    fun resetData(data: ArrayList<T>?) {
        this.data.clear()
        if (data != null && data.isNotEmpty()) {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun addData(data: ArrayList<T>?) {
        if (data == null || data.isEmpty()) {
            return
        }

        val size = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(size, data.size)
    }

    fun addItem(data: T) {
        val size = this.data.size
        this.data.add(data)
        notifyItemInserted(size)
    }

    fun clear() {
        this.data.clear()
    }

    abstract fun bindViewHolder(holder: ViewHolder, item: T, position: Int)
}