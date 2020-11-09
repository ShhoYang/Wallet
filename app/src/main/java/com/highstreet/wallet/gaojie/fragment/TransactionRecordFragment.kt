package com.highstreet.wallet.gaojie.fragment

import android.os.Bundle
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.adapter.TransactionRecordAdapter
import com.highstreet.wallet.gaojie.model.dip.Tx
import com.highstreet.wallet.gaojie.vm.TransactionRecordVM


/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */

class TransactionRecordFragment : BaseListFragment<Tx, TransactionRecordVM>() {

    private var isIn = true

    override fun prepare(savedInstanceState: Bundle?) {
        isIn = arguments?.getBoolean(ExtraKey.BOOLEAN, true) ?: true
    }

    override fun createAdapter() = TransactionRecordAdapter(isIn)

    override fun initData() {
        viewModel.isIn = isIn
        super.initData()
    }

    companion object {
        fun instance(isIn: Boolean): TransactionRecordFragment {
            val fragment = TransactionRecordFragment()
            val bundle = Bundle()
            bundle.putBoolean(ExtraKey.BOOLEAN, isIn)
            fragment.arguments = bundle
            return fragment
        }
    }
}