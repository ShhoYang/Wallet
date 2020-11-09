package com.highstreet.wallet.gaojie.model.dip

import android.text.TextUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class AccountInfo(
        val type: String?,
        val value: Value?
) {
    fun getAccountNumber(): Int {
        val account_number = value?.account_number
        if (account_number != null && TextUtils.isDigitsOnly(account_number)) {
            return account_number.toInt()
        }
        return 0
    }

    fun getSequence(): Int {
        val sequence = value?.sequence
        if (sequence != null && TextUtils.isDigitsOnly(sequence)) {
            return sequence.toInt()
        }
        return 0
    }

    fun getAmount(): String {
        val coins = value?.coins
        if (null != coins && coins.isNotEmpty()) {
            val coinBean = coins[0]
            return coinBean.amount + coinBean.denom
        }
        return ""
    }

    fun getLongAmount(): Long {
        val coins = value?.coins
        if (null != coins && coins.isNotEmpty()) {
            val coinBean = coins[0]
            return coinBean.amount?.toLong()?:0L
        }
        return 0L
    }
}

data class Value(
        val account_number: String?,
        val address: String?,
        val code_hash: String?,
        val coins: List<Coin>?,
        val public_key: PublicKey?,
        val sequence: String?
)

data class PublicKey(
        val type: String?,
        val value: String?
)