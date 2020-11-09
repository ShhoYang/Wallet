package com.highstreet.lib.adapter

import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.highstreet.lib.view.listener.RxView


/**
 * @author Yang Shihao
 * @date 2020/10/16
 */
abstract class BaseMultipleTypeAdapter<T>(data: ArrayList<T> = ArrayList()) : BaseNormalAdapter<T>(0,data) {

    private val delegates: SparseArrayCompat<ItemViewDelegate<T>> = SparseArrayCompat()

    fun addDelegate(delegate: ItemViewDelegate<T>): BaseMultipleTypeAdapter<T> {
        val viewType = delegates.size()
        delegates.put(viewType, delegate)
        return this
    }

    override fun bindViewHolder(holder: ViewHolder, item: T, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        val size = delegates.size()
        for (index in 0 until size) {
            val itemViewDelegate = delegates[index]
            if (itemViewDelegate!!.isViewType(getItem(position), position)) {
                return delegates.keyAt(index)
            }
        }
        throw IllegalArgumentException("No ItemViewDelegate added that matches position= $position in data source")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(context, parent, delegates.get(viewType)!!.getLayoutId())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemClickListener?.let {
            RxView.click(holder.itemView) {
                it.itemClicked(holder.itemView, getItem(position), position)
            }
        }
        delegates.get(holder.itemViewType)?.bindViewHolder(holder, getItem(position), position)
    }
}
