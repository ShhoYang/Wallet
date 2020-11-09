package com.highstreet.wallet.gaojie.adapter

import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.model.dip.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class ValidatorAdapter : BasePagedAdapter<Validator>(R.layout.g_item_validator) {


    override fun bindViewHolder(holder: ViewHolder, item: Validator, position: Int) {
        holder.setText(R.id.tvAvatar, item.getFirstLetterName())
                .setText(R.id.tvName, item.description?.moniker ?: "")
                .setText(R.id.tvShares, StringUtils.pdip2DIP(item.delegator_shares))
                .setText(R.id.tvAddress, item.operator_address ?: "")
                .setText(R.id.tvRate, item.getRate())
    }
}