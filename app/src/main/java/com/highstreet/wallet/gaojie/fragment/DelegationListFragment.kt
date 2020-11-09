package com.highstreet.wallet.gaojie.fragment

import android.arch.lifecycle.Observer
import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.activity.DelegationDetailActivity
import com.highstreet.wallet.gaojie.activity.ValidatorActivity
import com.highstreet.wallet.gaojie.adapter.DelegationAdapter
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo
import com.highstreet.wallet.gaojie.vm.DelegationListVm
import kotlinx.android.synthetic.main.g_fragment_delegation_list.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationListFragment : BaseListFragment<DelegationInfo, DelegationListVm>() {

    override fun getLayoutId() = R.layout.g_fragment_delegation_list


    override fun createAdapter() = DelegationAdapter()

    override fun initView() {
        super.initView()
        tvReceiveAddress.text = AccountManager.instance().address
        RxView.click(flValidator) {
            go(ValidatorActivity::class.java)
        }
    }

    override fun initData() {
        lifecycle.addObserver(viewModel)
        viewModel.totalLD.observe(this, Observer {
            tvDelegationAmount.text = StringUtils.pdip2DIP(it?.first)
            tvValidatorCount.text = it?.second
        })
        viewModel.rewardD.observe(this, Observer {
            tvReward.text = StringUtils.pdip2DIP(it)
        })
        super.initData()
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item) }
    }
}