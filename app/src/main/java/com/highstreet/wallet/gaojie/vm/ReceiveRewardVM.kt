package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.cosmos.MsgGenerator
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
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

class ReceiveRewardVM : BaseViewModel() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun receiveReward(validatorAddress: String, delegatorAddress: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            val coins = it.result?.value?.coins
            if (null != coins && coins.isNotEmpty()) {
                generateParams(it.result!!, validatorAddress, delegatorAddress)
            }
        }, {
            resultLD.value = Pair(false, "领取失败")
        }).add()
    }

    private fun generateParams(accountInfo: AccountInfo, validatorAddress: String, delegatorAddress: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val entropy = AccountManager.getEntropy(account)
        val deterministicKey = WKey.getKeyWithPathfromEntropy(AccountManager.instance().chain, entropy, account.path.toInt(), account.newBip44)
        val msg = MsgGenerator.genIncentiveReward(validatorAddress, delegatorAddress, AccountManager.instance().chain)
        val msgs = ArrayList<Msg>()
        msgs.add(msg)
        doReceiveReward(MsgGenerator.getBraodcaseReq(account, msgs, AmountUtils.generateFee(), "", deterministicKey))
    }

    private fun doReceiveReward(reqBroadCast: ReqBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                resultLD.value = Pair(true, "领取成功")
            } else {
                resultLD.value = Pair(false, "领取失败")
            }

        }, {
            resultLD.value = Pair(false, it)
        }).add()
    }
}