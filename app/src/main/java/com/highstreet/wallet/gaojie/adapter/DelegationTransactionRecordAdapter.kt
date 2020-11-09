package com.highstreet.wallet.gaojie.adapter

import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.model.dip.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordAdapter : BasePagedAdapter<Tx>(R.layout.g_item_common) {

    override fun bindViewHolder(holder: ViewHolder, item: Tx, position: Int) {
        val desc = StringBuilder()
        desc.append("金额：").append(StringUtils.pdip2DIP(item.getAmount())).append("\n")
                .append("时间：").append(StringUtils.utc2String(item.timestamp))
        holder.setText(R.id.tvTitle, "验证人地址：${item.getValidatorAddress()}")
                .setText(R.id.tvDesc, desc)
    }
}