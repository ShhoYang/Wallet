package com.highstreet.wallet.gaojie.adapter

import android.view.View
import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.model.dip.Proposal

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class ProposalAdapter : BasePagedAdapter<Proposal>(R.layout.g_item_proposal) {

    override fun bindViewHolder(holder: ViewHolder, item: Proposal, position: Int) {
        holder.setText(R.id.tvId, "#" + item.id)
                .setText(R.id.tvTitle, item?.content?.value?.title ?: "")
                .setText(R.id.tvStatus, item.getStatus())

        holder.getView<View>(R.id.statusPoint).setBackgroundResource(if (item.isPassed()) R.drawable.shape_circle_green else R.drawable.shape_circle_red)
    }
}