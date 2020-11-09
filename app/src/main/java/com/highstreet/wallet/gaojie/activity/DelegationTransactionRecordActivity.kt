package com.highstreet.wallet.gaojie.activity

import android.support.v4.app.Fragment
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.adapter.FragmentWithTabAdapter
import com.highstreet.wallet.gaojie.fragment.DelegationTransactionRecordFragment
import kotlinx.android.synthetic.main.g_activity_viewpager.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_viewpager

    override fun initView() {
        title = "委托交易记录"
        val fragments = arrayListOf<Pair<String, Fragment>>(
                Pair("委托", DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_BOND)),
                Pair("解委托", DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_UN_BOND)),
                Pair("重新委托", DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_REDELEGATE))
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager, fragments)
        tabLayout.setupWithViewPager(viewPager)
    }
}