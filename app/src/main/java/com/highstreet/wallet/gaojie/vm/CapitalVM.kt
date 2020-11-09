package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class CapitalVM : BalanceVM() {

    val delegationAmountLD = MutableLiveData<String>()

    fun getDelegationAmount() {
        ApiService.getDipApi().delegations(AccountManager.instance().address, 1, Int.MAX_VALUE).subscribeBy({
            handle(it.result)
        }, {
            handle(null)
        }).add()
    }

    private fun handle(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            delegationAmountLD.value = "0"
            return
        }
        var amount = 0L

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
        }

        delegationAmountLD.value = amount.toString()
    }
}