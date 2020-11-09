package com.highstreet.wallet.gaojie.adapter


import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class UnDelegationAdapter : BasePagedAdapter<DelegationInfo>(R.layout.g_item_common) {

    override fun bindViewHolder(holder: ViewHolder, item: DelegationInfo, position: Int) {
        val desc = StringBuilder()
        desc.append("初始金额：").append(StringUtils.pdip2DIP(item.shares)).append("\n")
                .append("余额：").append(StringUtils.pdip2DIP(item.balance)).append("\n")
                .append("剩余时间：").append(StringUtils.timeGap(item.completionTime))
        holder.setText(R.id.tvTitle, "验证人地址：${item.validator_address}")
                .setText(R.id.tvDesc, desc)
    }
}