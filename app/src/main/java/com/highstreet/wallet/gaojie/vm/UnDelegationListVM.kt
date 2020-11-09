package com.highstreet.wallet.gaojie.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.event.RefreshDelegationEvent
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Coin
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class UnDelegationListVM : BaseListViewModel<DelegationInfo>() {

    override fun onCreate() {
        super.onCreate()
        registerRxBus(RefreshDelegationEvent::class.java)
    }

    override fun loadData(page: Int, onResponse: (ArrayList<DelegationInfo>?) -> Unit) {
        ApiService.getDipApi().unBondingDelegations(AccountManager.instance().address, page, pageSize()).subscribeBy({
            onResponse(handle(it.result))
        }, {
            onResponse(null)
        }).add()
    }

    private fun handle(list: ArrayList<DelegationInfo>?): ArrayList<DelegationInfo>? {
        if (list == null || list.isEmpty()) {
            return list
        }

        val temp = ArrayList<DelegationInfo>()

        list.forEach { d ->
            d.entries?.forEach { e ->
                val delegationInfo = DelegationInfo(Coin(e.balance, ""), "", null, e.initial_balance, d.validator_address)
                delegationInfo.completionTime = e.completion_time
                temp.add(delegationInfo)
            }
        }

        return temp
    }
}