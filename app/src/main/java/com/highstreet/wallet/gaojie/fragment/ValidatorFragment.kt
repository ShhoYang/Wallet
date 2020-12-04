package com.highstreet.wallet.gaojie.fragment

import android.os.Bundle
import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.gaojie.activity.ValidatorDetailActivity
import com.highstreet.wallet.gaojie.adapter.ValidatorAdapter
import com.highstreet.wallet.gaojie.constant.Constant
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.vm.ValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorFragment : BaseListFragment<Validator, ValidatorVM>() {

    override fun createAdapter() = ValidatorAdapter()

    override fun initData() {
        viewModel.filterType = arguments?.getInt(ExtraKey.INT, Constant.VALIDATOR_ALL)
                ?: Constant.VALIDATOR_ALL
        super.initData()
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        context?.apply { ValidatorDetailActivity.start(this, item) }
    }

    companion object {
        fun instance(type: Int): ValidatorFragment {
            val fragment = ValidatorFragment()
            val bundle = Bundle()
            bundle.putInt(ExtraKey.INT, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}