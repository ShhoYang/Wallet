package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.AccountInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class BalanceVM : BaseViewModel() {

    val amountLD = MutableLiveData<AccountInfo>()

    fun getAccountInfo() {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            amountLD.value = it.result
        }, {
            amountLD.value = null
        }).add()
    }
}