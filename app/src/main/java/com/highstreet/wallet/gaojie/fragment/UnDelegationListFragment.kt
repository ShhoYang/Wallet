package com.highstreet.wallet.gaojie.fragment

import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.gaojie.activity.DelegationDetailActivity
import com.highstreet.wallet.gaojie.adapter.UnDelegationAdapter
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo
import com.highstreet.wallet.gaojie.vm.UnDelegationListVM


/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class UnDelegationListFragment : BaseListFragment<DelegationInfo, UnDelegationListVM>() {

    override fun createAdapter() = UnDelegationAdapter()

    override fun initData() {
        lifecycle.addObserver(viewModel)
        super.initData()
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item,true) }
    }
}