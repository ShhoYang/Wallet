package com.highstreet.wallet.gaojie.model.dip

import com.highstreet.lib.adapter.BaseItem
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class Proposal(
        val content: Content?,
        val deposit_end_time: String?,
        val final_tally_result: FinalTallyResult?,
        val id: String,
        val proposal_status: String,
        val proposer: String?,
        val submit_time: String?,
        val total_deposit: List<Coin>?,
        val voting_end_time: String?,
        val voting_start_time: String?
) : BaseItem, Serializable {

    fun getStatus(): String {
        return when (proposal_status) {
            "Nil" -> ""
            "DepositPeriod" -> "抵押阶段"
            "VotingPeriod" -> "投票阶段"
            "Passed" -> "通过"
            "Rejected" -> "未通过"
            "Failed" -> "未通过"
            else -> ""
        }
    }

    fun isPassed(): Boolean {
        return "Passed" == proposal_status
    }

    fun isVotingPeriod(): Boolean {
        return "VotingPeriod" == proposal_status
    }

    override fun uniqueKey(): String {
        return "$id"
    }
}

data class Content(
        val type: String?,
        val value: ProposalValue?
) : Serializable

data class FinalTallyResult(
        val abstain: String?,
        val no: String?,
        val no_with_veto: String?,
        val yes: String?
) : Serializable

data class ProposalValue(
        val changes: List<Change>?,
        val description: String?,
        val title: String?
) : Serializable

data class Change(
        val key: String?,
        val subspace: String?,
        val value: String?
) : Serializable