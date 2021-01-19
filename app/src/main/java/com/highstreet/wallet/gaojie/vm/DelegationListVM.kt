package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.event.RefreshDelegationEvent
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationListVM : BaseListViewModel<DelegationInfo>() {

    val totalLD: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val rewardD: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        registerRxBus(RefreshDelegationEvent::class.java)
    }

    override fun loadData(page: Int, onResponse: (ArrayList<DelegationInfo>?) -> Unit) {
        ApiService.getDipApi().delegations(AccountManager.instance().address, page, pageSize()).subscribeBy({
            onResponse(it.result)
            handle(it.result)
        }, {
            onResponse(null)
            handle(null)
        }).add()
    }

    private fun handle(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            totalLD.value = Pair("0", "0")
            return
        }
        var amount = 0L
        var validators = HashSet<String>()

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
            validators.add(it.validator_address)
        }

        totalLD.value = Pair(amount.toString(), validators.size.toString())
    }

    override fun refresh() {
        ApiService.getDipApi().rewards(AccountManager.instance().address).subscribeBy({
            val total = it?.result?.total
            rewardD.value = if (total == null || total.isEmpty()) {
                "0"
            } else {
                val coin = total[0]
               StringUtils.formatDecimal( coin.amount)
            }
        }, {
            rewardD.value = "0"
        }).add()
    }
}