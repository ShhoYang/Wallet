package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.RxBus
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.cosmos.MsgGenerator
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
import com.highstreet.wallet.gaojie.event.RefreshDelegationEvent
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.AccountInfo
import com.highstreet.wallet.model.type.Msg
import com.highstreet.wallet.network.req.ReqBroadCast
import com.highstreet.wallet.utils.WKey
import java.util.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class DelegationVM : BalanceVM() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun delegate(validationAddress: String, toAmount: String, remarks: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            val coins = it.result?.value?.coins
            if (null != coins && coins.isNotEmpty()) {
                val balance = coins[0].amount ?: "0"
                if (isEnough(balance, toAmount)) {
                    generateParams(it.result!!, validationAddress, toAmount, remarks)
                } else {
                    resultLD.value = Pair(false, "余额不足")
                }
            }
        }, {
        }).add()
    }

    private fun isEnough(balance: String, toAmount: String): Boolean {
        return balance.toLong() > toAmount.toLong()
    }

    private fun generateParams(accountInfo: AccountInfo, validationAddress: String, toAmount: String, remarks: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val entropy = AccountManager.getEntropy(account)
        val deterministicKey = WKey.getKeyWithPathfromEntropy(AccountManager.instance().chain, entropy, account.path.toInt(), account.newBip44)
        val msg = MsgGenerator.genDelegateMsg(account.address, validationAddress, AmountUtils.generateCoin(toAmount), AccountManager.instance().chain)
        val msgs = ArrayList<Msg>()
        msgs.add(msg)
        doDelegate(MsgGenerator.getBraodcaseReq(account, msgs, AmountUtils.generateFee(), remarks, deterministicKey))
    }

    private fun doDelegate(reqBroadCast: ReqBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                RxBus.instance().send(RefreshDelegationEvent())
                resultLD.value = Pair(true, "委托成功")
            } else {
                resultLD.value = Pair(false, "委托失败")
            }

        }, {
            resultLD.value = Pair(false, it)
        }).add()
    }
}