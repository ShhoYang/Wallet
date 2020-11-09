package com.highstreet.wallet.gaojie.fragment

import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.activity.ProposalDetailActivity
import com.highstreet.wallet.gaojie.adapter.ProposalAdapter
import com.highstreet.wallet.gaojie.model.dip.Proposal
import com.highstreet.wallet.gaojie.vm.ProposalVM


/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */

class ProposalFragment : BaseListFragment<Proposal, ProposalVM>() {

    override fun getLayoutId() = R.layout.g_fragment_proposal

    override fun createAdapter() = ProposalAdapter()

    override fun itemClicked(view: View, item: Proposal, position: Int) {
        activity?.apply {  ProposalDetailActivity.start(this, item) }
    }
}