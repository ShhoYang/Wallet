package com.highstreet.wallet.gaojie.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Proposal

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */
class ProposalVM : BaseListViewModel<Proposal>() {

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Proposal>?) -> Unit) {
        ApiService.getDipApi().proposals().subscribeBy({
            onResponse(it.result)
        }, {
            onResponse(null)
        }).add()

    }
}
