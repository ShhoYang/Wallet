package com.highstreet.wallet.gaojie

import com.highstreet.wallet.gaojie.constant.Constant
import com.highstreet.wallet.model.type.Coin
import com.highstreet.wallet.model.type.Fee
import java.math.BigDecimal

/**
 * @author Yang Shihao
 * @Date 2020/10/30
 */

object AmountUtils {

    /**
     * 费用
     */
    fun generateFee(): Fee {
        val coin = Coin()
        coin.denom = "pdip"
        coin.amount = BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS)).toString()
        val coinList = java.util.ArrayList<Coin>()
        coinList.add(coin)
        val fee = Fee()
        fee.amount = coinList
        fee.gas = "${Constant.GAS}"
        return fee
    }

    /**
     * @param amount DIP
     */
    fun generateCoin(amount: String): Coin {
        val coin = Coin()
        coin.denom = "pdip"
        coin.amount = BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE)).toLong().toString()
        return coin
    }

    /**
     * @param amount pdip
     */
    fun generateCoin(amount: Long, subtractFee: Boolean): Coin {
        val coin = Coin()
        coin.denom = "pdip"
        coin.amount = if (subtractFee) {
            BigDecimal(amount).subtract(BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS))).toLong().toString()
        } else {
            amount.toString()
        }
        return coin
    }

    /**
     * 余额是否足够
     * @param balance pdip
     * @param amount DIP
     */
    fun isEnough(balance: String, amount: String): Boolean {
        return BigDecimal(balance).compareTo(BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE))) > -1
    }
}