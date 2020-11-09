package com.highstreet.wallet.gaojie.fragment

import android.os.Bundle
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.gaojie.adapter.DelegationTransactionRecordAdapter
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.model.dip.Tx
import com.highstreet.wallet.gaojie.vm.DelegationTransactionRecordVm

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordFragment : BaseListFragment<Tx, DelegationTransactionRecordVm>() {

    override fun prepare(savedInstanceState: Bundle?) {
        viewModel.type = arguments?.getString(ExtraKey.STRING) ?: TYPE_BOND
    }

    override fun createAdapter() = DelegationTransactionRecordAdapter()

    companion object {

        const val TYPE_BOND = "bond"
        const val TYPE_UN_BOND = "unbond"
        const val TYPE_REDELEGATE = "redelegate"

        fun instance(type: String): DelegationTransactionRecordFragment {
            val fragment = DelegationTransactionRecordFragment()
            val bundle = Bundle()
            bundle.putString(ExtraKey.STRING, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}