package com.highstreet.wallet.gaojie.adapter

import android.widget.TextView
import com.highstreet.lib.adapter.BaseNormalAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class MnemonicAdapter(data: ArrayList<String>) : BaseNormalAdapter<String>(R.layout.g_item_mnemonic, data) {

    override fun bindViewHolder(holder: ViewHolder, item: String, position: Int) {
        (holder.itemView as TextView).text = item
    }
}