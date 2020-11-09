package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.lib.viewmodel.RxBus
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.cosmos.MsgGenerator
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
import com.highstreet.wallet.gaojie.event.RefreshDelegationEvent
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.AccountInfo
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo
import com.highstreet.wallet.model.type.Msg
import com.highstreet.wallet.network.req.ReqBroadCast
import com.highstreet.wallet.utils.WKey

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

class RedelegationVM : BaseViewModel() {

    val unDelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun redelegate(amount: String, delegationInfo: DelegationInfo, toValidatorAddress: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (it.result == null) {
                unDelegateLD.value = Pair(false, "转委托失败")
            } else {
                generateParams(it.result!!, amount, delegationInfo, toValidatorAddress)
            }
        }, {
        }).add()

    }

    private fun generateParams(accountInfo: AccountInfo, amount: String, delegationInfo: DelegationInfo, toValidatorAddress: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val entropy = AccountManager.getEntropy(account)
        val deterministicKey = WKey.getKeyWithPathfromEntropy(AccountManager.instance().chain, entropy, account.path.toInt(), account.newBip44)
        val msg = MsgGenerator.genReDelegateMsg(account.address, delegationInfo.validator_address, toValidatorAddress, AmountUtils.generateCoin(amount), AccountManager.instance().chain)
        val msgs = ArrayList<Msg>()
        msgs.add(msg)
        doRedelegate(MsgGenerator.getBraodcaseReq(account, msgs, AmountUtils.generateFee(), "", deterministicKey))
    }

    private fun doRedelegate(reqBroadCast: ReqBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                RxBus.instance().send(RefreshDelegationEvent())
                unDelegateLD.value = Pair(true, "转委托成功")
            } else {
                unDelegateLD.value = Pair(false, "转委托失败")
            }

        }, {
            unDelegateLD.value = Pair(false, it)
        }).add()
    }
}