package com.highstreet.wallet.gaojie.adapter

import com.highstreet.lib.adapter.BaseMultipleTypeAdapter
import com.highstreet.lib.adapter.ItemViewDelegate
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.model.dip.Menu

/**
 * @author Yang Shihao
 * @date 2020/10/20
 */
class MenuAdapter(data: ArrayList<Menu>) : BaseMultipleTypeAdapter<Menu>(data) {

    init {
        addDelegate(NormalItem())
        addDelegate(WideLineItem())
        addDelegate(NarrowLineItem())
    }

    inner class NormalItem : ItemViewDelegate<Menu> {
        override fun getLayoutId() = R.layout.g_item_menu

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_NORMAL

        override fun bindViewHolder(holder: ViewHolder, item: Menu, position: Int) {
            holder.setImageResource(R.id.ivIcon, item.icon)
                    .setText(R.id.tvText, item.text)
        }
    }

    inner class WideLineItem : ItemViewDelegate<Menu> {

        override fun getLayoutId() = R.layout.g_item_menu_wide_line

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_WIDE_LINE

        override fun bindViewHolder(holder: ViewHolder, item: Menu, position: Int) {
        }
    }

    inner class NarrowLineItem : ItemViewDelegate<Menu> {

        override fun getLayoutId() = R.layout.g_item_menu_narrow_line

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_NARROW_LINE

        override fun bindViewHolder(holder: ViewHolder, item: Menu, position: Int) {
        }
    }
}


