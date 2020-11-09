package com.highstreet.wallet.gaojie.fragment

import android.support.v4.app.Fragment
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.activity.DelegationTransactionRecordActivity
import com.highstreet.wallet.gaojie.adapter.FragmentWithTabAdapter
import kotlinx.android.synthetic.main.g_fragment_staking.*


/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class StakingFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.g_fragment_staking

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
                Pair("委托中", DelegationListFragment()),
                Pair("待解除", UnDelegationListFragment())
        )
        viewPager.adapter = FragmentWithTabAdapter(childFragmentManager, fragments)
        tabLayout.setupWithViewPager(viewPager)
        RxView.click(ivRecord) {
            go(DelegationTransactionRecordActivity::class.java)
        }
    }
}