package com.highstreet.wallet.gaojie.model.dip

/**
 * @author Yang Shihao
 * @Date 2020/10/26
 */
data class DelegateMsg(
        val delegator_address: String,
        val validator_address: String,
        val amount: Coin
)
