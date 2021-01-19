package com.highstreet.wallet.gaojie.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.fragment.DelegationTransactionRecordFragment
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordVM : BaseListViewModel<Tx>() {

    var type = DelegationTransactionRecordFragment.TYPE_BOND

    override fun loadData(page: Int, onResponse: (ArrayList<Tx>?) -> Unit) {
        ApiService.getDipApi().delegationTransactionRecord(AccountManager.instance().address, type, page, pageSize()).subscribeBy({
            onResponse(it.result?.get(0)?.txs)
        }, {
            onResponse(null)
        }).add()
    }
}