package com.highstreet.wallet.gaojie.activity

import android.support.v4.app.Fragment
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.adapter.FragmentWithTabAdapter
import com.highstreet.wallet.gaojie.fragment.ValidatorFragment
import kotlinx.android.synthetic.main.g_activity_viewpager.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_viewpager

    override fun initView() {
        title = "验证人"
        val fragments = arrayListOf<Pair<String, Fragment>>(
                Pair("共识中", ValidatorFragment.instance(ValidatorFragment.TYPE_BONDED)),
//                Pair("候选中", ValidatorFragment.instance(ValidatorFragment.TYPE_UN_BONDING)),
                Pair("待解禁", ValidatorFragment.instance(ValidatorFragment.TYPE_JAILED)),
                Pair("全部", ValidatorFragment.instance(ValidatorFragment.TYPE_ALL))
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager, fragments)
        tabLayout.setupWithViewPager(viewPager)
    }
}