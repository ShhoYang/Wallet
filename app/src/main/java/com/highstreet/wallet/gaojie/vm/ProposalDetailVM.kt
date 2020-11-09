package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.cosmos.MsgGenerator
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
import com.highstreet.wallet.gaojie.constant.ProposalOpinion
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.AccountInfo
import com.highstreet.wallet.gaojie.model.dip.Proposal
import com.highstreet.wallet.model.type.Msg
import com.highstreet.wallet.network.req.ReqBroadCast
import com.highstreet.wallet.utils.WKey
import java.util.*

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

class ProposalDetailVM : BaseViewModel() {

    val opinionLD = MutableLiveData<String>()

    val voteLD = MutableLiveData<Pair<Boolean, String>>()

    val proposalLD = MutableLiveData<Proposal>()

    private fun proposalDetail(proposalId: String) {
        ApiService.getDipApi().proposalDetail(proposalId).subscribeBy({
            proposalLD.value = it.result
        }, {
        }).add()
    }

    fun proposalOpinion(proposalId: String) {
        ApiService.getDipApi().proposalOpinion(proposalId, AccountManager.instance().address).subscribeBy({
            opinionLD.value = ProposalOpinion.getOpinion(it.result?.option)
        }, {
        }, false).add()
    }

    fun vote(proposalId: String, opinion: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            val accountInfo = it.result
            if (null == accountInfo) {
                voteLD.value = Pair(false, "投票失败")
            } else {
                generateParams(accountInfo, proposalId, opinion)
            }
        }, {
        }).add()
    }

    private fun generateParams(accountInfo: AccountInfo, proposalId: String, opinion: String) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val entropy = AccountManager.getEntropy(account)
        val deterministicKey = WKey.getKeyWithPathfromEntropy(AccountManager.instance().chain, entropy, account.path.toInt(), account.newBip44)
        val msg = MsgGenerator.genVoteMsg(account.address, proposalId, opinion, AccountManager.instance().chain)
        val msgs = ArrayList<Msg>()
        msgs.add(msg)
        doVote(MsgGenerator.getBraodcaseReq(account, msgs, AmountUtils.generateFee(), "", deterministicKey), proposalId)
    }

    private fun doVote(reqBroadCast: ReqBroadCast, proposalId: String) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                proposalOpinion(proposalId)
                proposalDetail(proposalId)
                voteLD.value = Pair(true, "投票成功")
            } else {
                voteLD.value = Pair(false, "投票失败")
            }

        }, {
            voteLD.value = Pair(false, it)
        }).add()
    }
}