package com.highstreet.wallet.gaojie.constant

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

object ProposalOpinion {
    const val YES = "Yes"
    const val NO = "No"
    const val NO_WITH_VETO = "NoWithVeto"
    const val ABSTAIN = "Abstain"

    fun getOpinion(s: String?): String {
        return when (s) {
            YES -> "同意"
            NO -> "不同意"
            NO_WITH_VETO -> "强烈反对"
            ABSTAIN -> "弃权"
            else -> ""
        }
    }
}