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

    fun generateFee(): Fee {
        val coin = Coin()
        coin.denom = "pdip"
//        coin.amount = "${Constant.GAS_PRICE * Constant.GAS}"
        coin.amount = BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS)).toString()
        val coinList = java.util.ArrayList<Coin>()
        coinList.add(coin)
        val fee = Fee()
        fee.amount = coinList
        fee.gas = "${Constant.GAS}"
        return fee
    }

    fun generateCoin(amount: String): Coin {
        val coin = Coin()
        coin.denom = "pdip"
        coin.amount = BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE)).toString()
        return coin
    }

    fun isEnough(balance: String, amount: String): Boolean {
        return BigDecimal(balance).compareTo(BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE))) > -1
    }
}