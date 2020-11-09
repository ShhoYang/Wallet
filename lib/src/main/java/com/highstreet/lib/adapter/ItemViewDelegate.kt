package com.highstreet.lib.adapter

/**
 * @author Yang Shihao
 * @date 2020/10/16
 */
interface ItemViewDelegate<T> {

    fun getLayoutId(): Int

    fun isViewType(item: T, position: Int): Boolean

    fun bindViewHolder(holder: ViewHolder, item: T, position: Int)
}