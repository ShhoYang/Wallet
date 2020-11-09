package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.cosmos.MsgGenerator
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.AccountInfo
import com.highstreet.wallet.model.type.Coin
import com.highstreet.wallet.model.type.Msg
import com.highstreet.wallet.network.req.ReqBroadCast
import com.highstreet.wallet.utils.WKey
import java.util.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class TransactionVM : BaseViewModel() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun transact(toAddress: String, toAmount: String) {

        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            val coins = it.result?.value?.coins
            if (null != coins && coins.isNotEmpty()) {
                val balance = coins[0].amount ?: "0"
                if (AmountUtils.isEnough(balance, toAmount)) {
                    generateParams(it.result!!, toAddress, toAmount)
                } else {
                    resultLD.value = Pair(false, "金额不足")
                }
            }
        }, {
        }).add()
    }

    private fun generateParams(accountInfo: AccountInfo, toAddress: String, toAmount: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val coinList = ArrayList<Coin>()
        coinList.add(AmountUtils.generateCoin(toAmount))
        val entropy = AccountManager.getEntropy(account)
        val deterministicKey = WKey.getKeyWithPathfromEntropy(AccountManager.instance().chain, entropy, account.path.toInt(), account.newBip44)
        val msg = MsgGenerator.genTransferMsg(account.address, toAddress, coinList, AccountManager.instance().chain)
        val msgs = ArrayList<Msg>()
        msgs.add(msg)
        doTransact(MsgGenerator.getBraodcaseReq(account, msgs, AmountUtils.generateFee(), "", deterministicKey))
    }

    private fun doTransact(reqBroadCast: ReqBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                resultLD.value = Pair(true, "交易成功")
            } else {
                resultLD.value = Pair(false, "交易失败")
            }

        }, {
            resultLD.value = Pair(false, it)
        }).add()
    }
}