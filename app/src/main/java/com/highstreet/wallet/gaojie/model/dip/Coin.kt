package com.highstreet.wallet.gaojie.model.dip

import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class Coin(
        val amount: String?,
        val denom: String?
) : Serializable {
    /**
     *  1DIP=10^12pdip
     */
    fun getShowAmount(): String {
        return if (amount?.length ?: 0 > 14) {
            "${amount!!.toLong() / 1_000_000_000_000}DIP"
        } else {
            amount + denom
        }
    }
}