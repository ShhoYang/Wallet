package com.highstreet.wallet.gaojie.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/19
 */
class TransactionRecordVM : BaseListViewModel<Tx>() {

    var isIn = true

    override fun loadData(page: Int, onResponse: (ArrayList<Tx>?) -> Unit) {
        if (isIn) {
            ApiService.getDipApi().transactionInRecord(AccountManager.instance().address, page, pageSize()).subscribeBy({
                onResponse(it.txs)
            }, {
                onResponse(null)
            }).add()
        } else {
            ApiService.getDipApi().transactionOutRecord(AccountManager.instance().address, page, pageSize()).subscribeBy({
                onResponse(it.txs)
            }, {
                onResponse(null)
            }).add()
        }
    }
}
